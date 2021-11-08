package pl.miernik.payitforward.institution;

import org.springframework.data.domain.Page;
import pl.miernik.payitforward.exception.NotExistingRecordException;

import java.util.List;

public interface IInstitutionService {
    //CRUD
    void save(InstitutionDto institutionDto);
    InstitutionDto getInstitutionDtoById(Long id) throws NotExistingRecordException;
    void update(InstitutionDto institutionDto) throws NotExistingRecordException;
    void delete(Long id) throws NotExistingRecordException;
    List<InstitutionDto> findAll();
    //Page<Institution> findPaginated(int pageNo, int pageSize);
    Page<Institution> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection);
}
