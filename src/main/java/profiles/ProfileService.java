package profiles;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by surkov on 21.12.14.
 */
public class ProfileService {
    private Map<Integer, VipProfile> vip = new HashMap<>();

    public Map<Integer, VipProfile> getVip() {
        return vip;
    }
}
