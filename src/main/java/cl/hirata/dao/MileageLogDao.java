package cl.hirata.dao;

import cl.hirata.model.MileageLog;
import java.util.List;

public interface MileageLogDao {

    MileageLog create(MileageLog mileageLog);

    List<MileageLog> findByTruckId(int truckId);
}
