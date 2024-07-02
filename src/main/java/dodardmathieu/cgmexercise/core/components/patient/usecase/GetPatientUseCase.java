package dodardmathieu.cgmexercise.core.components.patient.usecase;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import javax.validation.constraints.NotNull;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import dodardmathieu.cgmexercise.common.CgmExerciseResource;
import dodardmathieu.cgmexercise.core.providers.patient.PatientRepository;
import dodardmathieu.cgmexercise.dataproviders.patient.PatientEntity;
import dodardmathieu.cgmexercise.error.exception.ResourceNotFoundException;

@Service
public class GetPatientUseCase {

	private final PatientRepository patientRepository;
	
	@Autowired
	public GetPatientUseCase(PatientRepository patientRepository) {
		this.patientRepository = patientRepository;
	}
	
	@FunctionalInterface
	public interface GetPatientPresenter<T> {
		T apply(PatientEntity patientEntity);
	}
	
	@Transactional(readOnly = true)
	public <T> T getPatient(@NotNull UUID patientId, @NotNull GetPatientPresenter<T> presenter) {
		return patientRepository.find(patientId).map(presenter::apply).orElseThrow(() -> new ResourceNotFoundException(CgmExerciseResource.PATIENT, patientId.toString()));
	}
	
	@Transactional(readOnly = true)
	public <T> List<T> getAll(@NotNull GetPatientPresenter<T> presenter) {
		return patientRepository.findAll().stream().map(presenter::apply).collect(Collectors.toList());
	}
}
