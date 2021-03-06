package com.targaryen.octopus.service;

import com.targaryen.octopus.dao.*;
import com.targaryen.octopus.dto.*;
import com.targaryen.octopus.util.Role;
import com.targaryen.octopus.util.StatusCode;
import com.targaryen.octopus.vo.UserVo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityExistsException;
import javax.persistence.PersistenceException;

/**
 * Created by Liu Mengyang on 2018/09/03
 */

@Service
public class UserServiceImpl implements UserService {
    private UserDtoRepository userDtoRepository;
    private RoleDtoRepository roleDtoRepository;
    private ApplicantDtoRepository applicantDtoRepository;
    private InterviewerDtoRepository interviewerDtoRepository;
    private HRDtoRepository hrDtoRepository;
    private DptManagerDtoRepository dptManagerDtoRepository;
    private DepartmentDtoRepository departmentDtoRepository;

    @Autowired
    public UserServiceImpl(DaoFactory daoFactory) {
        this.userDtoRepository = daoFactory.getUserDtoRepository();
        this.roleDtoRepository = daoFactory.getRoleDtoRepository();
        this.applicantDtoRepository = daoFactory.getApplicantDtoRepository();
        this.interviewerDtoRepository = daoFactory.getInterviewerDtoRepository();
        this.hrDtoRepository = daoFactory.getHRDtoRepository();
        this.dptManagerDtoRepository = daoFactory.getDptManagerDtoRepository();
        this.departmentDtoRepository = daoFactory.getDepartmentDtoRepository();
    }

    /**
     *
     * @param userVo
     * "userId" and "userRole" fields are empty.
     */
    public int saveUser(UserVo userVo) {
        UserDto userDto = new UserDto();
        RoleDto roleDto = new RoleDto();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        DepartmentDto departmentDto;

        userDto.setUserName(userVo.getUserName());
        userDto.setUserPassword(encoder.encode(userVo.getUserPassword()));
        try {
            userDtoRepository.save(userDto);
        } catch (DataIntegrityViolationException e) {
            return StatusCode.DUPLICATE_KEY;
        }


        roleDto.setRole(userVo.getUserRole());
        roleDto.setUser(userDto);
        roleDtoRepository.save(roleDto);

        switch (roleDto.getRole()) {
            case Role.APPLICANT:
                ApplicantDto applicantDto = new ApplicantDto();
                applicantDto.setUser(userDto);
                applicantDtoRepository.save(applicantDto);
                break;
            case Role.DPT:
                DptManagerDto dptManagerDto = new DptManagerDto();
                departmentDto = departmentDtoRepository.findDepartmentDtoByDepartmentId(userVo.getDepartmentId());
                dptManagerDto.setUser(userDto);
                dptManagerDto.setDepartment(departmentDto);
                dptManagerDtoRepository.save(dptManagerDto);
                break;
            case Role.HR:
                HRDto hrDto = new HRDto();
                hrDto.setUser(userDto);
                hrDtoRepository.save(hrDto);
                break;
            case Role.INTERVIEWER:
                InterviewerDto interviewerDto = new InterviewerDto();
                departmentDto = departmentDtoRepository.findDepartmentDtoByDepartmentId(userVo.getDepartmentId());
                interviewerDto.setUser(userDto);
                interviewerDto.setDepartment(departmentDto);
                interviewerDtoRepository.save(interviewerDto);
                break;
            default:
        }

        return StatusCode.SUCCESS;
    }

    public UserVo getUserByUserName(String userName) {
        UserDto userDto;
        RoleDto roleDto;

        try {
            userDto = userDtoRepository.findUserDtoByUserName(userName);
        } catch (DataAccessException e) {
            return null;
        }

        roleDto = userDto.getRole();

        UserVo userVo = new UserVo.Builder().userId(userDto.getUserId()).userName(userDto.getUserName()).userPassword(userDto.getUserPassword()).userRole(userDto.getRole().getRole()).build();

        return userVo;
    }

    public boolean checkPassword(UserVo userVo) {
        UserDto userDto;

        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try {
            userDto = userDtoRepository.findUserDtoByUserId(userVo.getUserId());
            return encoder.matches(userVo.getUserPassword(), userDto.getUserPassword());
        } catch (DataAccessException e) {
            return false;
        }
    }

    public int editPassword(UserVo userVo) {
        UserDto userDto;
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();

        try {
            userDto = userDtoRepository.findUserDtoByUserId(userVo.getUserId());
            userDto.setUserPassword(encoder.encode(userVo.getUserPassword()));
            userDtoRepository.save(userDto);
        } catch (DataAccessException e) {
            return StatusCode.FAILURE;
        }

        return StatusCode.SUCCESS;
    }

}
