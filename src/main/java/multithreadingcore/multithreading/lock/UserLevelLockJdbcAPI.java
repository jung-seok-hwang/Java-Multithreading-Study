package multithreadingcore.multithreading.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.Callable;
import java.util.function.Supplier;


@Slf4j
@Component
@RequiredArgsConstructor
public class UserLevelLockJdbcAPI {

    private static final String GET_LOCK = "SELECT GET_LOCK(:userLockName, :timeoutSeconds)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(:userLockName)";
    private static final String EXCEPTION_EXTRACTOR = "LOCK EXCEPTION";

    private static final ResultSetExtractor<Integer> RESULT_SET_EXTRACTOR = rs -> {
        if (rs.next()) {
            return rs.getInt(1);
        }
        return null;
    };

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;

    public <T> T executeWithLock(
            String userLockName,
            Integer timeoutSeconds,
            Supplier<T> supplier
    ) {
        try {
            get(userLockName, timeoutSeconds);
            return supplier.get();
        } finally {
            releaseLock(userLockName);
        }
    }

    //get Lock
    public void get(String userLockName, Integer timeoutSeconds) {
        Map<String, Object> params = new HashMap<>();

        params.put("userLockName", userLockName);
        params.put("timeoutSeconds", timeoutSeconds);
        log.info("GetLock!! userLockName : [{}], timeoutSeconds : [{}]", userLockName, timeoutSeconds);
        Integer result = namedParameterJdbcTemplate.queryForObject(GET_LOCK, params, Integer.class);
        checkResult(result, userLockName, "ReleaseLock");
    }

    //자원 반납
    private void releaseLock(String userLockName) {
        Map<String, Object> params = new HashMap<>();
        params.put("userLockName", userLockName);
        Integer result = namedParameterJdbcTemplate.queryForObject(RELEASE_LOCK, params, Integer.class);
        checkResult(result, userLockName, "ReleaseLock");
    }

    private void checkResult(Integer result, String userLockName, String type) {
        if (result == null) {
            throw new RuntimeException("Lock is null");
        }

        if (result != 1) {
            log.error("USER LEVEL LOCK 쿼리 결과 값이 1이 아닙니다. type = [{}], result : [{}] userLockName : [{}]", type, result, userLockName);
            throw new RuntimeException("Lock result is not 1");
        }
    }


}
