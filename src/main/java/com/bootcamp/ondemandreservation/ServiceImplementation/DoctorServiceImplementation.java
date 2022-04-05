package com.bootcamp.ondemandreservation.ServiceImplementation;

import com.bootcamp.ondemandreservation.Model.Admin;
import com.bootcamp.ondemandreservation.Model.Appointment;
import com.bootcamp.ondemandreservation.Model.Doctor;
import com.bootcamp.ondemandreservation.Repository.DoctorRepository;
import com.bootcamp.ondemandreservation.Service.DoctorService;
import com.bootcamp.ondemandreservation.security.ODRPasswordEncoder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class DoctorServiceImplementation implements DoctorService {
    @Autowired
    private DoctorRepository doctorRepository;
    @Autowired
    private ODRPasswordEncoder odrPasswordEncoder;

    public DoctorServiceImplementation() {
    }

    public DoctorServiceImplementation(DoctorRepository doctorRepository, ODRPasswordEncoder odrPasswordEncoder) {
        this.doctorRepository = doctorRepository;
        this.odrPasswordEncoder = odrPasswordEncoder;
    }

    @Override
    public void changePassword(Long id, String plaintextPassword) {
        Doctor theDoctor=findDoctorById(id);
        theDoctor.setPassword(odrPasswordEncoder.defaultPasswordEncoder().encode(plaintextPassword));
        saveDoctor(theDoctor);
    }

    /**
     * Save doctor method
     * @param doctor doctor to save
     * @return Doctor as saved in the DB, nul;l if error
     */
    @Override
    public Doctor saveDoctor(Doctor doctor) {
        return doctorRepository.save(doctor);
    }

    @Override
    public List<Doctor> getAllDoctors() {
        return doctorRepository.findAll();
    }

    @Override
    public Doctor findDoctorById(Long id) {
        Optional<Doctor> doctor = doctorRepository.findById(id);
        return doctor.get();
    }

    /**
     * @param id passed from Controller to find specific doctor.
     *  To delete a Doctor object, doctor can not have any relations to other tables.
     *  This method removes relations to any Appointment list that a doctor might have relations to.
     *  Deletes doctor.
     */

    @Override
    public void deleteDoctor(Long id) {
        Doctor currentDoctor = findDoctorById(id);
        currentDoctor.removeDoctorFromAppointmentList();
        currentDoctor.removeDoctorFromSchedule();
        doctorRepository.deleteById(id);
    }

    @Override
    public List<Appointment> getAllAppointments(Long id) {
        Doctor doctor = findDoctorById(id);
        return doctor.getAppointmentList();
    }

    @Override
    public Doctor updateDoctor(Long id, Doctor doctor) {
        doctor.setId(id);
        return saveDoctor(doctor);
    }

    @Override
    public List<Appointment> getTodaysAppointments(Long id) {
        List<Appointment> AllAppointments = getAllAppointments(id);
        List<Appointment> todaysAppointments = new ArrayList<>();

        for (Appointment appointment: AllAppointments) {

            if(appointment.getAppointmentTime().getDayOfMonth() == LocalDateTime.now().getDayOfMonth()) {
                todaysAppointments.add(appointment);
            }

        }

        return todaysAppointments;
    }
}
