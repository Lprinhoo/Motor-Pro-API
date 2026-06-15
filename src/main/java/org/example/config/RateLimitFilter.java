package org.example.config;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.benmanes.caffeine.cache.Caffeine;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Refill;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit; // Import adicionado

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    // Substitui ConcurrentHashMap por Caffeine Cache
    private final Cache<String, Bucket> buckets = Caffeine.newBuilder()
            .expireAfterWrite(1, TimeUnit.MINUTES) // Define o TTL para 1 minuto (mesmo que o refill)
            .build();

    private Bucket createBucket() {
        // Permite 5 tentativas em 1 minuto
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        // Aplica o rate limit apenas para a rota de login
        if (!request.getRequestURI().equals("/api/auth/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        String ip = request.getRemoteAddr();
        // Usa Caffeine cache para obter ou criar o bucket
        Bucket bucket = buckets.get(ip, k -> createBucket());

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Muitas tentativas. Aguarde 1 minuto.\"}");
        }
    }
}