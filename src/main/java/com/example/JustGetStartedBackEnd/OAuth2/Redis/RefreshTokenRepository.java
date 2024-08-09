package com.example.JustGetStartedBackEnd.OAuth2.Redis;

import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Repository;

import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.TimeUnit;

@Repository
public class RefreshTokenRepository {

    private RedisTemplate redisTemplate;

    public RefreshTokenRepository(final RedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    public String save(final RefreshToken refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        valueOperations.set(refreshToken.getRefreshToken(), refreshToken.getEmail());
        redisTemplate.expire(refreshToken.getRefreshToken(), 3, TimeUnit.DAYS); // 3일 후에는 자동으로 삭제되도록 수정
        return "Success";
    }

    public Optional<RefreshToken> findByEmail(final String refreshToken) {
        ValueOperations<String, String> valueOperations = redisTemplate.opsForValue();
        String email = valueOperations.get(refreshToken);

        if (Objects.isNull(email)) {
            return Optional.empty();
        }

        return Optional.of(new RefreshToken(refreshToken, email));
    }

    public void delete(final String refreshToken) {
        redisTemplate.delete(refreshToken);
    }
}
