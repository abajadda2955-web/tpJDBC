package lab2.service;
import lab2.dao.MachineDao ;
import lab2.entities.Machine ;
import java.util.List;

public class MachineService {
    private final MachineDao dao = new MachineDao();

    public Machine getMachine(Machine m) throws Exception {
        return dao.findById(m.getId());
    }

    public List<Machine> listMachines() throws Exception {
        return dao.findAll();
    }

    public Machine createMachine(Machine m) throws Exception {
        dao.insert(m);
        return m;
    }

    public boolean updateMachine(Machine m) throws Exception {
        return dao.update(m);
    }

    public boolean deleteMachine(Machine m) throws Exception {
        return dao.delete(m.getId());
    }
}
