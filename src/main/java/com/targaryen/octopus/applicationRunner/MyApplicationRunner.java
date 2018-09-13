package com.targaryen.octopus.applicationRunner;

import com.targaryen.octopus.dto.ApplicantDto;
import com.targaryen.octopus.dto.DptManagerDto;
import com.targaryen.octopus.dto.PostDto;
import com.targaryen.octopus.service.ApplicantService;
import com.targaryen.octopus.service.DptManagerService;
import com.targaryen.octopus.service.ServiceFactory;
import com.targaryen.octopus.util.DataTransferUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class MyApplicationRunner implements ApplicationRunner {
    @Autowired
    JdbcTemplate template;

    private static final String SELECT_SQL = "SELECT department_id from t_department where department_id = 1";

    private static final String INSERT_SQL = "INSERT INTO t_department (department_id, department_name) VALUES (1, 'HUE Development') ON conflict(department_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (1, 'dpt_manager', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (2, 'hr', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (3, 'applicant_1', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (4, 'applicant_2', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (5, 'applicant_3', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (6, 'applicant_4', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (7, 'applicant_5', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (8, 'applicant_6', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (9, 'applicant_7', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (10, 'applicant_8', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (11, 'applicant_9', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (12, 'applicant_10', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (13, 'interviewer_1', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (14, 'interviewer_2', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (15, 'interviewer_3', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (16, 'interviewer_4', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_user VALUES (17, 'interviewer_5', '$2a$10$e4e9uIUe4L4i2enYkXbmiupCUsVNtpnkMPMQm10Mj0/pN0sl/zvQO') ON conflict(user_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (1, 'ROLE_DPT', 1) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (2, 'ROLE_HR', 2) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (3, 'ROLE_APPLICANT', 3) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (4, 'ROLE_APPLICANT', 4) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (5, 'ROLE_APPLICANT', 5) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (6, 'ROLE_APPLICANT', 6) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (7, 'ROLE_APPLICANT', 7) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (8, 'ROLE_APPLICANT', 8) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (9, 'ROLE_APPLICANT', 9) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (10, 'ROLE_APPLICANT', 10) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (11, 'ROLE_APPLICANT', 11) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (12, 'ROLE_APPLICANT', 12) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (13, 'ROLE_INTERVIEWER', 13) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (14, 'ROLE_INTERVIEWER', 14) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (15, 'ROLE_INTERVIEWER', 15) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (16, 'ROLE_INTERVIEWER', 16) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_role VALUES (17, 'ROLE_INTERVIEWER', 17) ON conflict(role_id) DO NOTHING;\n" +
            "INSERT INTO t_dpt_manager VALUES (1, 1, 1) ON conflict(dpt_manager_id) DO NOTHING;\n" +
            "INSERT INTO t_hr VALUES (1, 2) ON conflict(hr_id) DO NOTHING;\n" +
            "INSERT INTO t_applicant VALUES (1, 3) ON conflict(applicant_id) DO NOTHING;\n" +
            "INSERT INTO t_applicant VALUES (2, 4) ON conflict(applicant_id) DO NOTHING;\n" +
            "INSERT INTO t_applicant VALUES (3, 5) ON conflict(applicant_id) DO NOTHING;\n" +
            "INSERT INTO t_applicant VALUES (4, 6) ON conflict(applicant_id) DO NOTHING;\n" +
            "INSERT INTO t_applicant VALUES (5, 7) ON conflict(applicant_id) DO NOTHING;\n" +
            "INSERT INTO t_applicant VALUES (6, 8) ON conflict(applicant_id) DO NOTHING;\n" +
            "INSERT INTO t_applicant VALUES (7, 9) ON conflict(applicant_id) DO NOTHING;\n" +
            "INSERT INTO t_applicant VALUES (8, 10) ON conflict(applicant_id) DO NOTHING;\n" +
            "INSERT INTO t_applicant VALUES (9, 11) ON conflict(applicant_id) DO NOTHING;\n" +
            "INSERT INTO t_applicant VALUES (10, 12) ON conflict(applicant_id) DO NOTHING;\n" +
            "INSERT INTO t_interviewer  VALUES (1, 25, 'HUE Development', 'Interviewer_1', 'Java Developer', 1, 13) ON conflict(interviewer_id) DO NOTHING;\n" +
            "INSERT INTO t_interviewer  VALUES (2, 25, 'HUE Development', 'Interviewer_2', 'Java Developer', 1, 14) ON conflict(interviewer_id) DO NOTHING;\n" +
            "INSERT INTO t_interviewer  VALUES (3, 25, 'HUE Development', 'Interviewer_3', 'Java Developer', 1, 15) ON conflict(interviewer_id) DO NOTHING;\n" +
            "INSERT INTO t_interviewer  VALUES (4, 25, 'HUE Development', 'Interviewer_4', 'Java Developer', 1, 16) ON conflict(interviewer_id) DO NOTHING;\n" +
            "INSERT INTO t_interviewer  VALUES (5, 25, 'HUE Development', 'Interviewer_5', 'Java Developer', 1, 17) ON conflict(interviewer_id) DO NOTHING;\n" +
            "INSERT INTO t_post VALUES (1, 0, 'Java Developer', 'Shanghai', 'Java Developer', 'Java', 'R&D', '2018-09-01', 30, 0, 0, 1) ON conflict(post_id) DO NOTHING;\n" +
            "INSERT INTO t_application (application_id, create_time, status, applicant_id, post_id) VALUES (1, '2018-09-02', 1, 1, 1) ON conflict(application_id) DO NOTHING;\n" +
            "INSERT INTO t_application (application_id, create_time, status, applicant_id, post_id) VALUES (2, '2018-09-02', 1, 2, 1) ON conflict(application_id) DO NOTHING;\n" +
            "INSERT INTO t_application (application_id, create_time, status, applicant_id, post_id) VALUES (3, '2018-09-02', 1, 3, 1) ON conflict(application_id) DO NOTHING;\n" +
            "INSERT INTO t_application (application_id, create_time, status, applicant_id, post_id) VALUES (4, '2018-09-02', 1, 4, 1) ON conflict(application_id) DO NOTHING;\n" +
            "INSERT INTO t_application (application_id, create_time, status, applicant_id, post_id) VALUES (5, '2018-09-02', 1, 5, 1) ON conflict(application_id) DO NOTHING;\n" +
            "INSERT INTO t_application (application_id, create_time, status, applicant_id, post_id) VALUES (6, '2018-09-02', 1, 6, 1) ON conflict(application_id) DO NOTHING;\n" +
            "INSERT INTO t_application (application_id, create_time, status, applicant_id, post_id) VALUES (7, '2018-09-02', 1, 7, 1) ON conflict(application_id) DO NOTHING;\n" +
            "INSERT INTO t_application (application_id, create_time, status, applicant_id, post_id) VALUES (8, '2018-09-02', 1, 8, 1) ON conflict(application_id) DO NOTHING;\n" +
            "INSERT INTO t_application (application_id, create_time, status, applicant_id, post_id) VALUES (9, '2018-09-02', 1, 9, 1) ON conflict(application_id) DO NOTHING;\n" +
            "INSERT INTO t_application (application_id, create_time, status, applicant_id, post_id) VALUES (10, '2018-09-02', 1, 10, 1) ON conflict(application_id) DO NOTHING;\n" +
            "INSERT INTO t_resume (resume_id, applicant_name, applicant_age, applicant_current_salary, applicant_degree, applicant_expect_salary, applicant_sex, applicant_id) VALUES (1, 'applicant_1', 22, 0, 1, 10000, 0, 1) ON conflict(resume_id) DO NOTHING;\n" +
            "INSERT INTO t_resume (resume_id, applicant_name, applicant_age, applicant_current_salary, applicant_degree, applicant_expect_salary, applicant_sex, applicant_id) VALUES (2, 'applicant_2', 22, 0, 1, 10000, 0, 2) ON conflict(resume_id) DO NOTHING;\n" +
            "INSERT INTO t_resume (resume_id, applicant_name, applicant_age, applicant_current_salary, applicant_degree, applicant_expect_salary, applicant_sex, applicant_id) VALUES (3, 'applicant_3', 22, 0, 1, 10000, 0, 3) ON conflict(resume_id) DO NOTHING;\n" +
            "INSERT INTO t_resume (resume_id, applicant_name, applicant_age, applicant_current_salary, applicant_degree, applicant_expect_salary, applicant_sex, applicant_id) VALUES (4, 'applicant_4', 22, 0, 1, 10000, 0, 4) ON conflict(resume_id) DO NOTHING;\n" +
            "INSERT INTO t_resume (resume_id, applicant_name, applicant_age, applicant_current_salary, applicant_degree, applicant_expect_salary, applicant_sex, applicant_id) VALUES (5, 'applicant_5', 22, 0, 1, 10000, 0, 5) ON conflict(resume_id) DO NOTHING;\n" +
            "INSERT INTO t_resume (resume_id, applicant_name, applicant_age, applicant_current_salary, applicant_degree, applicant_expect_salary, applicant_sex, applicant_id) VALUES (6, 'applicant_6', 22, 0, 1, 10000, 1, 6) ON conflict(resume_id) DO NOTHING;\n" +
            "INSERT INTO t_resume (resume_id, applicant_name, applicant_age, applicant_current_salary, applicant_degree, applicant_expect_salary, applicant_sex, applicant_id) VALUES (7, 'applicant_7', 22, 0, 1, 10000, 1, 7) ON conflict(resume_id) DO NOTHING;\n" +
            "INSERT INTO t_resume (resume_id, applicant_name, applicant_age, applicant_current_salary, applicant_degree, applicant_expect_salary, applicant_sex, applicant_id) VALUES (8, 'applicant_8', 22, 0, 1, 10000, 1, 8) ON conflict(resume_id) DO NOTHING;\n" +
            "INSERT INTO t_resume (resume_id, applicant_name, applicant_age, applicant_current_salary, applicant_degree, applicant_expect_salary, applicant_sex, applicant_id) VALUES (9, 'applicant_9', 22, 0, 1, 10000, 1, 9) ON conflict(resume_id) DO NOTHING;\n" +
            "INSERT INTO t_resume (resume_id, applicant_name, applicant_age, applicant_current_salary, applicant_degree, applicant_expect_salary, applicant_sex, applicant_id) VALUES (10, 'applicant_10', 22, 0, 1, 10000, 1, 10) ON conflict(resume_id) DO NOTHING;";

    @Override
    public void run(ApplicationArguments args) throws Exception {
        List<Integer> department_id =template.query(SELECT_SQL, ps -> {}, (rs, rowNum) -> rs.getInt(1));
        if(department_id.size() == 0) {
            template.update(INSERT_SQL, ps -> {});
        }
    }
}
