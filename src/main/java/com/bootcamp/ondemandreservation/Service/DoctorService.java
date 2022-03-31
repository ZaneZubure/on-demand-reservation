package com.bootcamp.ondemandreservation.Service;

import com.bootcamp.ondemandreservation.Model.Appointment;
import com.bootcamp.ondemandreservation.Model.Doctor;

import java.util.List;

public interface DoctorService {

    Doctor saveDoctor(Doctor doctor);
    List<Doctor> getAllDoctors();
    Doctor findDoctorById(Long id);
    void deleteDoctor(Long id);
    List<Appointment> getAllAppointments(Long id);
    Doctor updateDoctor(Long id, Doctor doctor);
}
