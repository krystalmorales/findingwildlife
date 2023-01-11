package learn.findingwildlife.data;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class KnownGoodState {

    @Autowired
    JdbcTemplate template;

    static boolean hasRun = false;

    void set() {
        if(!hasRun) {
            hasRun = true;
            template.update("call set_known_good_state();");
        }
    }
}
