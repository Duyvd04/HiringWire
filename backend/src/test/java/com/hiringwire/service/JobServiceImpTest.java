package com.hiringwire.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.ArgumentMatchers.isNull;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.hiringwire.dto.Application;
import com.hiringwire.exception.HiringWireException;
import com.hiringwire.mapper.ApplicantMapper;
import com.hiringwire.mapper.JobMapper;
import com.hiringwire.model.Applicant;
import com.hiringwire.model.ApplicationStatus;
import com.hiringwire.model.Job;
import com.hiringwire.model.Profile;
import com.hiringwire.model.User;
import com.hiringwire.model.enums.AccountStatus;
import com.hiringwire.model.enums.AccountType;
import com.hiringwire.model.enums.JobStatus;
import com.hiringwire.model.request.ApplicantRequest;
import com.hiringwire.model.request.JobRequest;
import com.hiringwire.model.request.NotificationRequest;
import com.hiringwire.model.response.JobResponse;
import com.hiringwire.repository.JobRepository;
import com.hiringwire.repository.UserRepository;

import java.io.UnsupportedEncodingException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.aot.DisabledInAotMode;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ContextConfiguration(classes = {JobServiceImpl.class})
@ExtendWith(SpringExtension.class)
@DisabledInAotMode
class JobServiceImpTest {
    @MockBean
    private ApplicantMapper applicantMapper;

    @MockBean
    private JavaMailSender javaMailSender;

    @MockBean
    private JobMapper jobMapper;

    @MockBean
    private JobRepository jobRepository;

    @Autowired
    private JobServiceImpl jobServiceImpl;

    @MockBean
    private NotificationService notificationService;

    @MockBean
    private PdfGeneratorService pdfGeneratorService;

    @MockBean
    private ResumeParser resumeParser;

    @MockBean
    private UserRepository userRepository;

    /**
     * Method under test: {@link JobServiceImpl#postJob(JobRequest)}
     */
    @Test
    void testPostJob() throws HiringWireException {
        // Arrange
        Job job = new Job();
        job.setAbout("About");
        job.setApplicants(new ArrayList<>());
        job.setCompany("Company");
        job.setDescription("The characteristics of someone or something");
        job.setExperience("Experience");
        job.setId(1L);
        job.setJobStatus(JobStatus.ACTIVE);
        job.setJobTitle("Dr");
        job.setJobType("Job Type");
        job.setLocation("Location");
        job.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job.setPostedBy(1L);
        job.setSalary(1L);
        job.setSkillsRequired(new ArrayList<>());
        Optional<Job> ofResult = Optional.of(job);

        Job job2 = new Job();
        job2.setAbout("About");
        job2.setApplicants(new ArrayList<>());
        job2.setCompany("Company");
        job2.setDescription("The characteristics of someone or something");
        job2.setExperience("Experience");
        job2.setId(1L);
        job2.setJobStatus(JobStatus.ACTIVE);
        job2.setJobTitle("Dr");
        job2.setJobType("Job Type");
        job2.setLocation("Location");
        job2.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job2.setPostedBy(1L);
        job2.setSalary(1L);
        job2.setSkillsRequired(new ArrayList<>());
        when(jobRepository.save(Mockito.<Job>any())).thenReturn(job2);
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Job job3 = new Job();
        job3.setAbout("About");
        job3.setApplicants(new ArrayList<>());
        job3.setCompany("Company");
        job3.setDescription("The characteristics of someone or something");
        job3.setExperience("Experience");
        job3.setId(1L);
        job3.setJobStatus(JobStatus.ACTIVE);
        job3.setJobTitle("Dr");
        job3.setJobType("Job Type");
        job3.setLocation("Location");
        job3.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job3.setPostedBy(1L);
        job3.setSalary(1L);
        job3.setSkillsRequired(new ArrayList<>());

        JobResponse jobResponse = new JobResponse();
        jobResponse.setAbout("About");
        jobResponse.setApplicants(new ArrayList<>());
        jobResponse.setCompany("Company");
        jobResponse.setDescription("The characteristics of someone or something");
        jobResponse.setExperience("Experience");
        jobResponse.setId(1L);
        jobResponse.setJobStatus(JobStatus.ACTIVE);
        jobResponse.setJobTitle("Dr");
        jobResponse.setJobType("Job Type");
        jobResponse.setLocation("Location");
        jobResponse.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        jobResponse.setPostedBy(1L);
        jobResponse.setSalary(1L);
        jobResponse.setSkillsRequired(new ArrayList<>());
        when(jobMapper.toResponse(Mockito.<Job>any())).thenReturn(jobResponse);
        when(jobMapper.toEntity(Mockito.<JobRequest>any())).thenReturn(job3);

        JobRequest jobRequest = new JobRequest();
        jobRequest.setAbout("About");
        jobRequest.setCompany("Company");
        jobRequest.setDescription("The characteristics of someone or something");
        jobRequest.setExperience("Experience");
        jobRequest.setId(1L);
        jobRequest.setJobStatus(JobStatus.ACTIVE);
        jobRequest.setJobTitle("Dr");
        jobRequest.setJobType("Job Type");
        jobRequest.setLocation("Location");
        jobRequest.setPostedBy(1L);
        jobRequest.setSalary(1L);
        jobRequest.setSkillsRequired(new ArrayList<>());

        // Act
        JobResponse actualPostJobResult = jobServiceImpl.postJob(jobRequest);

        // Assert
        verify(jobMapper).toEntity(isA(JobRequest.class));
        verify(jobMapper).toResponse(isA(Job.class));
        verify(jobRepository).findById(eq(1L));
        verify(jobRepository).save(isA(Job.class));
        assertSame(jobResponse, actualPostJobResult);
    }

    /**
     * Method under test: {@link JobServiceImpl#postJob(JobRequest)}
     */
    @Test
    void testPostJob2() throws HiringWireException {
        // Arrange
        Job job = new Job();
        job.setAbout("About");
        job.setApplicants(new ArrayList<>());
        job.setCompany("Company");
        job.setDescription("The characteristics of someone or something");
        job.setExperience("Experience");
        job.setId(1L);
        job.setJobStatus(JobStatus.DRAFT);
        job.setJobTitle("Dr");
        job.setJobType("Job Type");
        job.setLocation("Location");
        job.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job.setPostedBy(1L);
        job.setSalary(1L);
        job.setSkillsRequired(new ArrayList<>());
        Optional<Job> ofResult = Optional.of(job);

        Job job2 = new Job();
        job2.setAbout("About");
        job2.setApplicants(new ArrayList<>());
        job2.setCompany("Company");
        job2.setDescription("The characteristics of someone or something");
        job2.setExperience("Experience");
        job2.setId(1L);
        job2.setJobStatus(JobStatus.ACTIVE);
        job2.setJobTitle("Dr");
        job2.setJobType("Job Type");
        job2.setLocation("Location");
        job2.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job2.setPostedBy(1L);
        job2.setSalary(1L);
        job2.setSkillsRequired(new ArrayList<>());
        when(jobRepository.save(Mockito.<Job>any())).thenReturn(job2);
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Job job3 = new Job();
        job3.setAbout("About");
        job3.setApplicants(new ArrayList<>());
        job3.setCompany("Company");
        job3.setDescription("The characteristics of someone or something");
        job3.setExperience("Experience");
        job3.setId(1L);
        job3.setJobStatus(JobStatus.ACTIVE);
        job3.setJobTitle("Dr");
        job3.setJobType("Job Type");
        job3.setLocation("Location");
        job3.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job3.setPostedBy(1L);
        job3.setSalary(1L);
        job3.setSkillsRequired(new ArrayList<>());

        JobResponse jobResponse = new JobResponse();
        jobResponse.setAbout("About");
        jobResponse.setApplicants(new ArrayList<>());
        jobResponse.setCompany("Company");
        jobResponse.setDescription("The characteristics of someone or something");
        jobResponse.setExperience("Experience");
        jobResponse.setId(1L);
        jobResponse.setJobStatus(JobStatus.ACTIVE);
        jobResponse.setJobTitle("Dr");
        jobResponse.setJobType("Job Type");
        jobResponse.setLocation("Location");
        jobResponse.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        jobResponse.setPostedBy(1L);
        jobResponse.setSalary(1L);
        jobResponse.setSkillsRequired(new ArrayList<>());
        when(jobMapper.toResponse(Mockito.<Job>any())).thenReturn(jobResponse);
        when(jobMapper.toEntity(Mockito.<JobRequest>any())).thenReturn(job3);

        JobRequest jobRequest = new JobRequest();
        jobRequest.setAbout("About");
        jobRequest.setCompany("Company");
        jobRequest.setDescription("The characteristics of someone or something");
        jobRequest.setExperience("Experience");
        jobRequest.setId(1L);
        jobRequest.setJobStatus(JobStatus.ACTIVE);
        jobRequest.setJobTitle("Dr");
        jobRequest.setJobType("Job Type");
        jobRequest.setLocation("Location");
        jobRequest.setPostedBy(1L);
        jobRequest.setSalary(1L);
        jobRequest.setSkillsRequired(new ArrayList<>());

        // Act
        JobResponse actualPostJobResult = jobServiceImpl.postJob(jobRequest);

        // Assert
        verify(jobMapper).toEntity(isA(JobRequest.class));
        verify(jobMapper).toResponse(isA(Job.class));
        verify(jobRepository).findById(eq(1L));
        verify(jobRepository).save(isA(Job.class));
        assertSame(jobResponse, actualPostJobResult);
    }

    /**
     * Method under test: {@link JobServiceImpl#postJob(JobRequest)}
     */
    @Test
    void testPostJob3() throws HiringWireException {
        // Arrange
        Optional<Job> emptyResult = Optional.empty();
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        JobRequest jobRequest = new JobRequest();
        jobRequest.setAbout("About");
        jobRequest.setCompany("Company");
        jobRequest.setDescription("The characteristics of someone or something");
        jobRequest.setExperience("Experience");
        jobRequest.setId(1L);
        jobRequest.setJobStatus(JobStatus.ACTIVE);
        jobRequest.setJobTitle("Dr");
        jobRequest.setJobType("Job Type");
        jobRequest.setLocation("Location");
        jobRequest.setPostedBy(1L);
        jobRequest.setSalary(1L);
        jobRequest.setSkillsRequired(new ArrayList<>());

        // Act and Assert
        assertThrows(HiringWireException.class, () -> jobServiceImpl.postJob(jobRequest));
        verify(jobRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link JobServiceImpl#getAllJobs()}
     */
    @Test
    void testGetAllJobs() throws HiringWireException {
        // Arrange
        when(jobRepository.findAll()).thenReturn(new ArrayList<>());

        // Act and Assert
        assertThrows(HiringWireException.class, () -> jobServiceImpl.getAllJobs());
        verify(jobRepository).findAll();
    }

    /**
     * Method under test: {@link JobServiceImpl#getAllJobs()}
     */
    @Test
    void testGetAllJobs2() throws HiringWireException {
        // Arrange
        Job job = new Job();
        job.setAbout("NO_JOBS_FOUND");
        job.setApplicants(new ArrayList<>());
        job.setCompany("NO_JOBS_FOUND");
        job.setDescription("The characteristics of someone or something");
        job.setExperience("NO_JOBS_FOUND");
        job.setId(1L);
        job.setJobStatus(JobStatus.ACTIVE);
        job.setJobTitle("Dr");
        job.setJobType("NO_JOBS_FOUND");
        job.setLocation("NO_JOBS_FOUND");
        job.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job.setPostedBy(1L);
        job.setSalary(1L);
        job.setSkillsRequired(new ArrayList<>());

        ArrayList<Job> jobList = new ArrayList<>();
        jobList.add(job);
        when(jobRepository.findAll()).thenReturn(jobList);

        JobResponse jobResponse = new JobResponse();
        jobResponse.setAbout("About");
        jobResponse.setApplicants(new ArrayList<>());
        jobResponse.setCompany("Company");
        jobResponse.setDescription("The characteristics of someone or something");
        jobResponse.setExperience("Experience");
        jobResponse.setId(1L);
        jobResponse.setJobStatus(JobStatus.ACTIVE);
        jobResponse.setJobTitle("Dr");
        jobResponse.setJobType("Job Type");
        jobResponse.setLocation("Location");
        jobResponse.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        jobResponse.setPostedBy(1L);
        jobResponse.setSalary(1L);
        jobResponse.setSkillsRequired(new ArrayList<>());
        when(jobMapper.toResponse(Mockito.<Job>any())).thenReturn(jobResponse);

        // Act
        List<JobResponse> actualAllJobs = jobServiceImpl.getAllJobs();

        // Assert
        verify(jobMapper).toResponse(isA(Job.class));
        verify(jobRepository).findAll();
        assertEquals(1, actualAllJobs.size());
        assertSame(jobResponse, actualAllJobs.get(0));
    }

    /**
     * Method under test: {@link JobServiceImpl#getJob(Long)}
     */
    @Test
    void testGetJob() throws HiringWireException {
        // Arrange
        Job job = new Job();
        job.setAbout("About");
        job.setApplicants(new ArrayList<>());
        job.setCompany("Company");
        job.setDescription("The characteristics of someone or something");
        job.setExperience("Experience");
        job.setId(1L);
        job.setJobStatus(JobStatus.ACTIVE);
        job.setJobTitle("Dr");
        job.setJobType("Job Type");
        job.setLocation("Location");
        job.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job.setPostedBy(1L);
        job.setSalary(1L);
        job.setSkillsRequired(new ArrayList<>());
        Optional<Job> ofResult = Optional.of(job);
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        JobResponse jobResponse = new JobResponse();
        jobResponse.setAbout("About");
        jobResponse.setApplicants(new ArrayList<>());
        jobResponse.setCompany("Company");
        jobResponse.setDescription("The characteristics of someone or something");
        jobResponse.setExperience("Experience");
        jobResponse.setId(1L);
        jobResponse.setJobStatus(JobStatus.ACTIVE);
        jobResponse.setJobTitle("Dr");
        jobResponse.setJobType("Job Type");
        jobResponse.setLocation("Location");
        jobResponse.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        jobResponse.setPostedBy(1L);
        jobResponse.setSalary(1L);
        jobResponse.setSkillsRequired(new ArrayList<>());
        when(jobMapper.toResponse(Mockito.<Job>any())).thenReturn(jobResponse);

        // Act
        JobResponse actualJob = jobServiceImpl.getJob(1L);

        // Assert
        verify(jobMapper).toResponse(isA(Job.class));
        verify(jobRepository).findById(eq(1L));
        assertSame(jobResponse, actualJob);
    }

    /**
     * Method under test: {@link JobServiceImpl#getJob(Long)}
     */
    @Test
    void testGetJob2() throws HiringWireException {
        // Arrange
        Optional<Job> emptyResult = Optional.empty();
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(HiringWireException.class, () -> jobServiceImpl.getJob(1L));
        verify(jobRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link JobServiceImpl#applyJob(Long, ApplicantRequest)}
     */
    @Test
    void testApplyJob() throws HiringWireException, UnsupportedEncodingException {
        // Arrange
        Job job = new Job();
        job.setAbout("name");
        job.setApplicants(new ArrayList<>());
        job.setCompany("name");
        job.setDescription("The characteristics of someone or something");
        job.setExperience("name");
        job.setId(1L);
        job.setJobStatus(JobStatus.ACTIVE);
        job.setJobTitle("Dr");
        job.setJobType("name");
        job.setLocation("name");
        job.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job.setPostedBy(1L);
        job.setSalary(1L);
        job.setSkillsRequired(new ArrayList<>());

        Profile profile = new Profile();
        profile.setAbout("name");
        profile.setAccountType("3");
        profile.setCertifications(new ArrayList<>());
        profile.setCompany("name");
        profile.setEmail("jane.doe@example.org");
        profile.setExperiences(new ArrayList<>());
        profile.setId(1L);
        profile.setJobTitle("Dr");
        profile.setLocation("name");
        profile.setName("name");
        profile.setPicture(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        profile.setSavedJobs(new ArrayList<>());
        profile.setSkills(new ArrayList<>());
        profile.setTotalExp(1L);

        User user = new User();
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setAccountType(AccountType.APPLICANT);
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setLastLoginDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setName("name");
        user.setPassword("iloveyou");
        user.setProfile(profile);

        Applicant applicant = new Applicant();
        applicant.setApplicationStatus(ApplicationStatus.APPLIED);
        applicant.setCoverLetter("name");
        applicant.setExtractedResume(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        applicant.setId(1L);
        applicant.setInterviewTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        applicant.setJob(job);
        applicant.setPhone("6625550144");
        applicant.setResume(new byte[]{'A', 1, 'A', 1, 'A', 1, 'A', 1});
        applicant.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        applicant.setUser(user);
        applicant.setWebsite("name");

        ArrayList<Applicant> applicants = new ArrayList<>();
        applicants.add(applicant);

        Job job2 = new Job();
        job2.setAbout("About");
        job2.setApplicants(applicants);
        job2.setCompany("Company");
        job2.setDescription("The characteristics of someone or something");
        job2.setExperience("Experience");
        job2.setId(1L);
        job2.setJobStatus(JobStatus.ACTIVE);
        job2.setJobTitle("Dr");
        job2.setJobType("Job Type");
        job2.setLocation("Location");
        job2.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job2.setPostedBy(1L);
        job2.setSalary(1L);
        job2.setSkillsRequired(new ArrayList<>());
        Optional<Job> ofResult = Optional.of(job2);
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        Profile profile2 = new Profile();
        profile2.setAbout("About");
        profile2.setAccountType("3");
        profile2.setCertifications(new ArrayList<>());
        profile2.setCompany("Company");
        profile2.setEmail("jane.doe@example.org");
        profile2.setExperiences(new ArrayList<>());
        profile2.setId(1L);
        profile2.setJobTitle("Dr");
        profile2.setLocation("Location");
        profile2.setName("Name");
        profile2.setPicture("AXAXAXAX".getBytes("UTF-8"));
        profile2.setSavedJobs(new ArrayList<>());
        profile2.setSkills(new ArrayList<>());
        profile2.setTotalExp(1L);

        User user2 = new User();
        user2.setAccountStatus(AccountStatus.ACTIVE);
        user2.setAccountType(AccountType.APPLICANT);
        user2.setEmail("jane.doe@example.org");
        user2.setId(1L);
        user2.setLastLoginDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user2.setName("Name");
        user2.setPassword("iloveyou");
        user2.setProfile(profile2);
        Optional<User> ofResult2 = Optional.of(user2);
        when(userRepository.findById(Mockito.<Long>any())).thenReturn(ofResult2);

        ApplicantRequest applicantRequest = new ApplicantRequest();
        applicantRequest.setApplicantId(1L);
        applicantRequest.setApplicationStatus(ApplicationStatus.APPLIED);
        applicantRequest.setCoverLetter("Cover Letter");
        applicantRequest.setEmail("jane.doe@example.org");
        applicantRequest.setExtractedResume("Extracted Resume");
        applicantRequest.setId(1L);
        applicantRequest.setInterviewTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        applicantRequest.setName("Name");
        applicantRequest.setPhone("6625550144");
        applicantRequest.setResume("Resume");
        applicantRequest.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        applicantRequest.setWebsite("Website");

        // Act and Assert
        assertThrows(HiringWireException.class, () -> jobServiceImpl.applyJob(1L, applicantRequest));
        verify(jobRepository).findById(eq(1L));
        verify(userRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link JobServiceImpl#getHistory(Long, ApplicationStatus)}
     */
    @Test
    void testGetHistory() {
        // Arrange
        when(jobRepository.findByApplicantIdAndApplicationStatus(Mockito.<Long>any(), Mockito.<ApplicationStatus>any()))
                .thenReturn(new ArrayList<>());

        // Act
        List<JobResponse> actualHistory = jobServiceImpl.getHistory(1L, ApplicationStatus.APPLIED);

        // Assert
        verify(jobRepository).findByApplicantIdAndApplicationStatus(eq(1L), eq(ApplicationStatus.APPLIED));
        assertTrue(actualHistory.isEmpty());
    }

    /**
     * Method under test: {@link JobServiceImpl#getJobsPostedBy(Long)}
     */
    @Test
    void testGetJobsPostedBy() throws HiringWireException {
        // Arrange
        when(jobRepository.findByPostedBy(Mockito.<Long>any())).thenReturn(new ArrayList<>());

        // Act
        List<JobResponse> actualJobsPostedBy = jobServiceImpl.getJobsPostedBy(1L);

        // Assert
        verify(jobRepository).findByPostedBy(eq(1L));
        assertTrue(actualJobsPostedBy.isEmpty());
    }

    /**
     * Method under test: {@link JobServiceImpl#changeAppStatus(Application)}
     */
    @Test
    void testChangeAppStatus() throws HiringWireException {
        // Arrange
        Job job = new Job();
        job.setAbout("About");
        job.setApplicants(new ArrayList<>());
        job.setCompany("Company");
        job.setDescription("The characteristics of someone or something");
        job.setExperience("Experience");
        job.setId(1L);
        job.setJobStatus(JobStatus.ACTIVE);
        job.setJobTitle("Dr");
        job.setJobType("Job Type");
        job.setLocation("Location");
        job.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job.setPostedBy(1L);
        job.setSalary(1L);
        job.setSkillsRequired(new ArrayList<>());
        Optional<Job> ofResult = Optional.of(job);
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(HiringWireException.class, () -> jobServiceImpl.changeAppStatus(new Application()));
        verify(jobRepository).findById(isNull());
    }

    /**
     * Method under test: {@link JobServiceImpl#changeAppStatus(Application)}
     */
    @Test
    void testChangeAppStatus2() throws HiringWireException, UnsupportedEncodingException {
        // Arrange
        Job job = new Job();
        job.setAbout("APPLICANT_NOT_FOUND");
        job.setApplicants(new ArrayList<>());
        job.setCompany("APPLICANT_NOT_FOUND");
        job.setDescription("The characteristics of someone or something");
        job.setExperience("APPLICANT_NOT_FOUND");
        job.setId(1L);
        job.setJobStatus(JobStatus.ACTIVE);
        job.setJobTitle("Dr");
        job.setJobType("APPLICANT_NOT_FOUND");
        job.setLocation("APPLICANT_NOT_FOUND");
        job.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job.setPostedBy(1L);
        job.setSalary(1L);
        job.setSkillsRequired(new ArrayList<>());

        Profile profile = new Profile();
        profile.setAbout("APPLICANT_NOT_FOUND");
        profile.setAccountType("3");
        profile.setCertifications(new ArrayList<>());
        profile.setCompany("APPLICANT_NOT_FOUND");
        profile.setEmail("jane.doe@example.org");
        profile.setExperiences(new ArrayList<>());
        profile.setId(1L);
        profile.setJobTitle("Dr");
        profile.setLocation("APPLICANT_NOT_FOUND");
        profile.setName("APPLICANT_NOT_FOUND");
        profile.setPicture("AXAXAXAX".getBytes("UTF-8"));
        profile.setSavedJobs(new ArrayList<>());
        profile.setSkills(new ArrayList<>());
        profile.setTotalExp(1L);

        User user = new User();
        user.setAccountStatus(AccountStatus.ACTIVE);
        user.setAccountType(AccountType.APPLICANT);
        user.setEmail("jane.doe@example.org");
        user.setId(1L);
        user.setLastLoginDate(LocalDate.of(1970, 1, 1).atStartOfDay());
        user.setName("APPLICANT_NOT_FOUND");
        user.setPassword("iloveyou");
        user.setProfile(profile);

        Applicant applicant = new Applicant();
        applicant.setApplicationStatus(ApplicationStatus.APPLIED);
        applicant.setCoverLetter("APPLICANT_NOT_FOUND");
        applicant.setExtractedResume("AXAXAXAX".getBytes("UTF-8"));
        applicant.setId(1L);
        applicant.setInterviewTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        applicant.setJob(job);
        applicant.setPhone("6625550144");
        applicant.setResume("AXAXAXAX".getBytes("UTF-8"));
        applicant.setTimestamp(LocalDate.of(1970, 1, 1).atStartOfDay());
        applicant.setUser(user);
        applicant.setWebsite("APPLICANT_NOT_FOUND");

        ArrayList<Applicant> applicants = new ArrayList<>();
        applicants.add(applicant);

        Job job2 = new Job();
        job2.setAbout("About");
        job2.setApplicants(applicants);
        job2.setCompany("Company");
        job2.setDescription("The characteristics of someone or something");
        job2.setExperience("Experience");
        job2.setId(1L);
        job2.setJobStatus(JobStatus.ACTIVE);
        job2.setJobTitle("Dr");
        job2.setJobType("Job Type");
        job2.setLocation("Location");
        job2.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job2.setPostedBy(1L);
        job2.setSalary(1L);
        job2.setSkillsRequired(new ArrayList<>());
        Optional<Job> ofResult = Optional.of(job2);
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act and Assert
        assertThrows(HiringWireException.class, () -> jobServiceImpl.changeAppStatus(new Application()));
        verify(jobRepository).findById(isNull());
    }

    /**
     * Method under test: {@link JobServiceImpl#changeAppStatus(Application)}
     */
    @Test
    void testChangeAppStatus3() throws HiringWireException {
        // Arrange
        Optional<Job> emptyResult = Optional.empty();
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(HiringWireException.class, () -> jobServiceImpl.changeAppStatus(new Application()));
        verify(jobRepository).findById(isNull());
    }

    /**
     * Method under test: {@link JobServiceImpl#deleteJob(Long)}
     */
    @Test
    void testDeleteJob() throws HiringWireException {
        // Arrange
        doNothing().when(jobRepository).deleteById(Mockito.<Long>any());

        // Act
        jobServiceImpl.deleteJob(1L);

        // Assert that nothing has changed
        verify(jobRepository).deleteById(eq(1L));
    }

    /**
     * Method under test: {@link JobServiceImpl#getJobWithApplicant(Long, Long)}
     */
    @Test
    void testGetJobWithApplicant() throws HiringWireException {
        // Arrange
        Job job = new Job();
        job.setAbout("About");
        job.setApplicants(new ArrayList<>());
        job.setCompany("Company");
        job.setDescription("The characteristics of someone or something");
        job.setExperience("Experience");
        job.setId(1L);
        job.setJobStatus(JobStatus.ACTIVE);
        job.setJobTitle("Dr");
        job.setJobType("Job Type");
        job.setLocation("Location");
        job.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job.setPostedBy(1L);
        job.setSalary(1L);
        job.setSkillsRequired(new ArrayList<>());
        Optional<Job> ofResult = Optional.of(job);
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);

        // Act
        Job actualJobWithApplicant = jobServiceImpl.getJobWithApplicant(1L, 1L);

        // Assert
        verify(jobRepository).findById(eq(1L));
        assertSame(job, actualJobWithApplicant);
    }

    /**
     * Method under test: {@link JobServiceImpl#getJobWithApplicant(Long, Long)}
     */
    @Test
    void testGetJobWithApplicant2() throws HiringWireException {
        // Arrange
        Optional<Job> emptyResult = Optional.empty();
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(HiringWireException.class, () -> jobServiceImpl.getJobWithApplicant(1L, 1L));
        verify(jobRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link JobServiceImpl#getPendingJobs()}
     */
    @Test
    void testGetPendingJobs() throws HiringWireException {
        // Arrange
        when(jobRepository.findByJobStatus(Mockito.<JobStatus>any())).thenReturn(new ArrayList<>());

        // Act
        List<JobResponse> actualPendingJobs = jobServiceImpl.getPendingJobs();

        // Assert
        verify(jobRepository).findByJobStatus(eq(JobStatus.PENDING));
        assertTrue(actualPendingJobs.isEmpty());
    }

    /**
     * Method under test: {@link JobServiceImpl#approveJob(Long)}
     */
    @Test
    void testApproveJob() throws HiringWireException {
        // Arrange
        Job job = new Job();
        job.setAbout("About");
        job.setApplicants(new ArrayList<>());
        job.setCompany("Company");
        job.setDescription("The characteristics of someone or something");
        job.setExperience("Experience");
        job.setId(1L);
        job.setJobStatus(JobStatus.ACTIVE);
        job.setJobTitle("Dr");
        job.setJobType("Job Type");
        job.setLocation("Location");
        job.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job.setPostedBy(1L);
        job.setSalary(1L);
        job.setSkillsRequired(new ArrayList<>());
        Optional<Job> ofResult = Optional.of(job);

        Job job2 = new Job();
        job2.setAbout("About");
        job2.setApplicants(new ArrayList<>());
        job2.setCompany("Company");
        job2.setDescription("The characteristics of someone or something");
        job2.setExperience("Experience");
        job2.setId(1L);
        job2.setJobStatus(JobStatus.ACTIVE);
        job2.setJobTitle("Dr");
        job2.setJobType("Job Type");
        job2.setLocation("Location");
        job2.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job2.setPostedBy(1L);
        job2.setSalary(1L);
        job2.setSkillsRequired(new ArrayList<>());
        when(jobRepository.save(Mockito.<Job>any())).thenReturn(job2);
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(notificationService).sendNotification(Mockito.<NotificationRequest>any());

        // Act
        jobServiceImpl.approveJob(1L);

        // Assert
        verify(notificationService).sendNotification(isA(NotificationRequest.class));
        verify(jobRepository).findById(eq(1L));
        verify(jobRepository).save(isA(Job.class));
    }

    /**
     * Method under test: {@link JobServiceImpl#approveJob(Long)}
     */
    @Test
    void testApproveJob2() throws HiringWireException {
        // Arrange
        Job job = new Job();
        job.setAbout("About");
        job.setApplicants(new ArrayList<>());
        job.setCompany("Company");
        job.setDescription("The characteristics of someone or something");
        job.setExperience("Experience");
        job.setId(1L);
        job.setJobStatus(JobStatus.ACTIVE);
        job.setJobTitle("Dr");
        job.setJobType("Job Type");
        job.setLocation("Location");
        job.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job.setPostedBy(1L);
        job.setSalary(1L);
        job.setSkillsRequired(new ArrayList<>());
        Optional<Job> ofResult = Optional.of(job);

        Job job2 = new Job();
        job2.setAbout("About");
        job2.setApplicants(new ArrayList<>());
        job2.setCompany("Company");
        job2.setDescription("The characteristics of someone or something");
        job2.setExperience("Experience");
        job2.setId(1L);
        job2.setJobStatus(JobStatus.ACTIVE);
        job2.setJobTitle("Dr");
        job2.setJobType("Job Type");
        job2.setLocation("Location");
        job2.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job2.setPostedBy(1L);
        job2.setSalary(1L);
        job2.setSkillsRequired(new ArrayList<>());
        when(jobRepository.save(Mockito.<Job>any())).thenReturn(job2);
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doThrow(new HiringWireException("An error occurred")).when(notificationService)
                .sendNotification(Mockito.<NotificationRequest>any());

        // Act and Assert
        assertThrows(HiringWireException.class, () -> jobServiceImpl.approveJob(1L));
        verify(notificationService).sendNotification(isA(NotificationRequest.class));
        verify(jobRepository).findById(eq(1L));
        verify(jobRepository).save(isA(Job.class));
    }

    /**
     * Method under test: {@link JobServiceImpl#approveJob(Long)}
     */
    @Test
    void testApproveJob3() throws HiringWireException {
        // Arrange
        Optional<Job> emptyResult = Optional.empty();
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(HiringWireException.class, () -> jobServiceImpl.approveJob(1L));
        verify(jobRepository).findById(eq(1L));
    }

    /**
     * Method under test: {@link JobServiceImpl#rejectJob(Long)}
     */
    @Test
    void testRejectJob() throws HiringWireException {
        // Arrange
        Job job = new Job();
        job.setAbout("About");
        job.setApplicants(new ArrayList<>());
        job.setCompany("Company");
        job.setDescription("The characteristics of someone or something");
        job.setExperience("Experience");
        job.setId(1L);
        job.setJobStatus(JobStatus.ACTIVE);
        job.setJobTitle("Dr");
        job.setJobType("Job Type");
        job.setLocation("Location");
        job.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job.setPostedBy(1L);
        job.setSalary(1L);
        job.setSkillsRequired(new ArrayList<>());
        Optional<Job> ofResult = Optional.of(job);

        Job job2 = new Job();
        job2.setAbout("About");
        job2.setApplicants(new ArrayList<>());
        job2.setCompany("Company");
        job2.setDescription("The characteristics of someone or something");
        job2.setExperience("Experience");
        job2.setId(1L);
        job2.setJobStatus(JobStatus.ACTIVE);
        job2.setJobTitle("Dr");
        job2.setJobType("Job Type");
        job2.setLocation("Location");
        job2.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job2.setPostedBy(1L);
        job2.setSalary(1L);
        job2.setSkillsRequired(new ArrayList<>());
        when(jobRepository.save(Mockito.<Job>any())).thenReturn(job2);
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doNothing().when(notificationService).sendNotification(Mockito.<NotificationRequest>any());

        // Act
        jobServiceImpl.rejectJob(1L);

        // Assert
        verify(notificationService).sendNotification(isA(NotificationRequest.class));
        verify(jobRepository).findById(eq(1L));
        verify(jobRepository).save(isA(Job.class));
    }

    /**
     * Method under test: {@link JobServiceImpl#rejectJob(Long)}
     */
    @Test
    void testRejectJob2() throws HiringWireException {
        // Arrange
        Job job = new Job();
        job.setAbout("About");
        job.setApplicants(new ArrayList<>());
        job.setCompany("Company");
        job.setDescription("The characteristics of someone or something");
        job.setExperience("Experience");
        job.setId(1L);
        job.setJobStatus(JobStatus.ACTIVE);
        job.setJobTitle("Dr");
        job.setJobType("Job Type");
        job.setLocation("Location");
        job.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job.setPostedBy(1L);
        job.setSalary(1L);
        job.setSkillsRequired(new ArrayList<>());
        Optional<Job> ofResult = Optional.of(job);

        Job job2 = new Job();
        job2.setAbout("About");
        job2.setApplicants(new ArrayList<>());
        job2.setCompany("Company");
        job2.setDescription("The characteristics of someone or something");
        job2.setExperience("Experience");
        job2.setId(1L);
        job2.setJobStatus(JobStatus.ACTIVE);
        job2.setJobTitle("Dr");
        job2.setJobType("Job Type");
        job2.setLocation("Location");
        job2.setPostTime(LocalDate.of(1970, 1, 1).atStartOfDay());
        job2.setPostedBy(1L);
        job2.setSalary(1L);
        job2.setSkillsRequired(new ArrayList<>());
        when(jobRepository.save(Mockito.<Job>any())).thenReturn(job2);
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(ofResult);
        doThrow(new HiringWireException("An error occurred")).when(notificationService)
                .sendNotification(Mockito.<NotificationRequest>any());

        // Act and Assert
        assertThrows(HiringWireException.class, () -> jobServiceImpl.rejectJob(1L));
        verify(notificationService).sendNotification(isA(NotificationRequest.class));
        verify(jobRepository).findById(eq(1L));
        verify(jobRepository).save(isA(Job.class));
    }

    /**
     * Method under test: {@link JobServiceImpl#rejectJob(Long)}
     */
    @Test
    void testRejectJob3() throws HiringWireException {
        // Arrange
        Optional<Job> emptyResult = Optional.empty();
        when(jobRepository.findById(Mockito.<Long>any())).thenReturn(emptyResult);

        // Act and Assert
        assertThrows(HiringWireException.class, () -> jobServiceImpl.rejectJob(1L));
        verify(jobRepository).findById(eq(1L));
    }
}
