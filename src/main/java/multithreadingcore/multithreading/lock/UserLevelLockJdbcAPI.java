package multithreadingcore.multithreading.lock;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;


@Slf4j
@Component
@RequiredArgsConstructor
public class UserLevelLockJdbcAPI {

    private static final String GET_LOCK = "SELECT GET_LOCK(:userLockName, :timeoutSeconds)";
    private static final String RELEASE_LOCK = "SELECT RELEASE_LOCK(:userLockName)";
    private static final String LOCK_ERROR_MSG = "Error during lock operation.";
    private static final ResultSetExtractor<Integer> RESULT_SET_EXTRACTOR = rs -> rs.next() ? rs.getInt(1) : null;

    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;


    public <T> T executeWithLock(String userLockName, int timeoutSeconds, Supplier<T> supplier) {
        try {
            getLock(userLockName, timeoutSeconds);
            return supplier.get();
        } finally {
            releaseLock(userLockName);
        }
    }

    private void getLock(String userLockName, int timeoutSeconds) {
        Map<String, Object> params = new HashMap<>();
        params.put("userLockName", userLockName);
        params.put("timeoutSeconds", timeoutSeconds);
        log.info("Acquiring lock: userLockName = [{}], timeoutSeconds = [{}]", userLockName, timeoutSeconds);
        Integer result = namedParameterJdbcTemplate.queryForObject(GET_LOCK, params, Integer.class);
        checkResult(result, userLockName, "GetLock");
    }

    private void releaseLock(String userLockName) {
        Map<String, Object> params = new HashMap<>();
        params.put("userLockName", userLockName);
        log.info("Releasing lock: userLockName = [{}]", userLockName);
        Integer result = namedParameterJdbcTemplate.queryForObject(RELEASE_LOCK, params, Integer.class);
        checkResult(result, userLockName, "ReleaseLock");
    }

    private void checkResult(Integer result, String userLockName, String type) {
        if (result == null) {
            log.error("No result for the lock operation. type = [{}], userLockName = [{}]", type, userLockName);
            throw new RuntimeException(LOCK_ERROR_MSG + " No result returned.");
        }
        if (result != 1) {
            log.error("Lock operation failed. type = [{}], result = [{}], userLockName = [{}]", type, result, userLockName);
            throw new RuntimeException(LOCK_ERROR_MSG + " Expected result 1, got " + result + ".");
        }
    }
}