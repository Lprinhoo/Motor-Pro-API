package org.example.config;

import com.github.benmanes.caffeine.cache.Caffeine;
import com.github.benmanes.caffeine.cache.LoadingCache;
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
import java.util.Set;
import java.util.concurrent.TimeUnit;

@Component
public class RateLimitFilter extends OncePerRequestFilter {

    // Endpoints sensíveis a brute-force / abuso (login e criação de contas)
    private static final Set<String> RATE_LIMITED_PATHS = Set.of(
            "/api/auth/login",
            "/api/auth/register"
    );

    // Usar LoadingCache do Caffeine para gerenciar os buckets com expiração
    private final LoadingCache<String, Bucket> buckets;

    public RateLimitFilter() {
        this.buckets = Caffeine.newBuilder()
                .expireAfterWrite(5, TimeUnit.MINUTES) // Os buckets expiram 5 minutos após a última escrita
                .build(this::createBucket); // Cria um novo bucket se não existir
    }

    private Bucket createBucket(String key) { // O método createBucket agora recebe uma chave
        Bandwidth limit = Bandwidth.classic(5, Refill.greedy(5, Duration.ofMinutes(1)));
        return Bucket.builder().addLimit(limit).build();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();
        if (!RATE_LIMITED_PATHS.contains(path)) {
            filterChain.doFilter(request, response);
            return;
        }

        // Chave por IP + endpoint, para que tentativas de login e de registro
        // tenham limites independentes
        String key = request.getRemoteAddr() + ":" + path;
        Bucket bucket = buckets.get(key); // Obtém o bucket, criando um novo se não existir ou estiver expirado

        if (bucket.tryConsume(1)) {
            filterChain.doFilter(request, response);
        } else {
            response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Muitas tentativas. Aguarde 1 minuto.\"}");
        }
    }
}