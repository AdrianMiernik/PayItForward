package pl.miernik.payitforward.institution;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import pl.miernik.payitforward.exception.NotExistingRecordException;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class InstitutionService implements IInstitutionService {
    private final InstitutionRepository institutionRepository;
    private final InstitutionMapper institutionMapper;

    @Override
    public void save(InstitutionDto institutionDto) {
        Institution institution = institutionMapper.dtoToInstitution(institutionDto);
        institutionRepository.save(institution);
    }

    @Override
    public InstitutionDto getInstitutionDtoById(Long id) throws NotExistingRecordException {
        return institutionRepository.findById(id).map(institutionMapper::institutionToDto)
                .orElseThrow(new NotExistingRecordException("Institution with id " + id + " does not exist."));
    }

    // Additional get method which get institution by id - used in methods: update ,
    // delete
    private Institution getInstitution(Long id) throws NotExistingRecordException {
        return institutionRepository.findById(id)
                .orElseThrow(new NotExistingRecordException("Institution with id " + id + " does not exist!"));
    }

    @Override
    public void update(InstitutionDto institutionDto) throws NotExistingRecordException {
        Institution institution = getInstitution(institutionDto.getId());
        institution.setName(institutionDto.getName());
        institution.setDescription(institutionDto.getDescription());
        institution.setCity(institutionDto.getCity());
        institutionRepository.save(institution);
    }

    @Override
    public void delete(Long id) throws NotExistingRecordException {
        Institution institution = getInstitution(id);
        institutionRepository.delete(institution);
    }

    @Override
    public List<InstitutionDto> findAll() {
        return institutionRepository.findAll().stream().map(institutionMapper::institutionToDto)
                .collect(Collectors.toList());
    }

    @Override
    public Page<Institution> findPaginated(int pageNo, int pageSize, String sortField, String sortDirection) {
        Sort sort = sortDirection.equalsIgnoreCase(Sort.Direction.ASC.name()) ? Sort.by(sortField).ascending() :
                Sort.by(sortField).descending();

        Pageable pageable = PageRequest.of(pageNo - 1, pageSize, sort);
        return this.institutionRepository.findAll(pageable);
    }

    /*
     * //Manual conversion
     *
     * @Override public void save(InstitutionDto institutionDto) { Institution
     * institution = new Institution( institutionDto.getId(),
     * institutionDto.getName(), institutionDto.getDescription());
     * institutionRepository.save(institution); }
     *
     * @Override public InstitutionDto getInstitutionDtoById(Long id) throws
     * NotExistingRecordException { Institution institution =
     * institutionRepository.getById(id); InstitutionDto institutionDto = new
     * InstitutionDto( institution.getId(), institution.getName(),
     * institution.getDescription()); return institutionDto; }
     */
}