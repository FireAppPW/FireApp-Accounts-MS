package pw.ersms.accounts.config;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;


import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;

@Component
public class JwtTokenFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtUtil;

    private boolean hasAuthorizationBearer(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (ObjectUtils.isEmpty(header) || !header.startsWith("Bearer")) {
            return false;
        }

        return true;
    }

    private String getAccessToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        String token = header.split(" ")[1].trim();
        return token;
    }

    private void setAuthenticationContext(String token, HttpServletRequest request) {
        UserDetails userDetails = getUserDetails(token);
        String departmentId = jwtUtil.getDepartmentIdFromToken(token);

        UsernamePasswordAuthenticationToken
                authentication = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        authentication.setDetails(
                new WebAuthenticationDetailsSource().buildDetails(request));

        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    private UserDetails getUserDetails(String token) {

        Claims claims = jwtUtil.parseClaims(token);
        String subject = (String) claims.get(Claims.SUBJECT);
        String role = (String) claims.get("roles");
        String departmentId = jwtUtil.getDepartmentIdFromToken(token);

        ArrayList<SimpleGrantedAuthority> authorities = new ArrayList<>();
        authorities.add(new SimpleGrantedAuthority(role));
        authorities.add(new SimpleGrantedAuthority(departmentId));
        String[] jwtSubject = subject.split(",");

        UserDetails userDetails = new User(jwtSubject[1], "", authorities);
        return userDetails;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (!hasAuthorizationBearer(request)) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getAccessToken(request);

        if (!jwtUtil.validateAccessToken(token)) {
            filterChain.doFilter(request, response);
            return;
        }

        String departmentIdFromURL = extractDepartmentIdFromRequest(request);
        String departmentIdFromToken = jwtUtil.getDepartmentIdFromToken(token);

        if(departmentIdFromURL == null) {
            setAuthenticationContext(token, request);
            filterChain.doFilter(request, response);
            return;
        }

        if (departmentIdFromToken == null || !departmentIdFromURL.equals(departmentIdFromToken)) {
            System.out.println("Invalid departmentId");
            System.out.println("departmentIdFromURL: " + departmentIdFromURL);
            System.out.println("departmentIdFromToken: " + departmentIdFromToken);
            response.sendError(HttpServletResponse.SC_FORBIDDEN, "Invalid departmentId");
            return;
        }

        setAuthenticationContext(token, request);
        filterChain.doFilter(request, response);
    }

    private String extractDepartmentIdFromRequest(HttpServletRequest request) {
        String path = request.getRequestURI();
        String[] pathParts = path.split("/");
        // Assuming the departmentId is at index 2 in the pathParts array
        // e.g. /account/departmentId/other/path/parts
        return (pathParts.length > 2) ? pathParts[2] : null;
    }
}
