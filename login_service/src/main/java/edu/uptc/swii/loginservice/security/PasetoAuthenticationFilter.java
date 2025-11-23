package edu.uptc.swii.loginservice.security;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class PasetoAuthenticationFilter extends OncePerRequestFilter {
    private final PasetoService pasetoService;
    private final ObjectMapper mapper = new ObjectMapper();

    public PasetoAuthenticationFilter(PasetoService pasetoService) {
        this.pasetoService = pasetoService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws ServletException, IOException {

        String header = request.getHeader("Authorization");
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);
            try {
                String payload = pasetoService.parseToken(token);
                JsonNode node = mapper.readTree(payload);
                String username = node.has("sub") ? node.get("sub").asText() : null;
                String role = node.has("role") ? node.get("role").asText() : null;
                long exp = node.has("exp") ? node.get("exp").asLong() : 0L;
                if (username != null && InstantNowOk(exp)) {
                    List<SimpleGrantedAuthority> authorities = new ArrayList<>();
                    if (role != null) {
                        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
                    }
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(username, null, authorities);
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            } catch (Exception e) {
                // invalid token -> ignore and continue (request will be unauthenticated)
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean InstantNowOk(long expEpoch) {
        if (expEpoch <= 0) return true;
        long now = System.currentTimeMillis() / 1000L;
        return now < expEpoch;
    }
}
