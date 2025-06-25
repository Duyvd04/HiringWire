//package com.hiringwire;
//
//import com.hiringwire.dto.Application;
//import com.hiringwire.dto.NotificationDTO;
//import com.hiringwire.exception.HiringWireException;
//import com.hiringwire.mapper.ApplicantMapper;
//import com.hiringwire.mapper.JobMapper;
//import com.hiringwire.model.*;
//import com.hiringwire.model.enums.JobStatus;
//import com.hiringwire.model.request.ApplicantRequest;
//import com.hiringwire.model.request.JobRequest;
//import com.hiringwire.model.response.JobResponse;
//import com.hiringwire.repository.JobRepository;
//import com.hiringwire.repository.UserRepository;
//import com.hiringwire.service.JobServiceImpl;
//import com.hiringwire.service.NotificationService;
//import com.hiringwire.service.PdfGeneratorService;
//import com.hiringwire.service.ResumeParser;
//import jakarta.mail.internet.MimeMessage;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.*;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.javamail.MimeMessageHelper;
//
//import java.time.LocalDateTime;
//import java.util.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//import static org.mockito.Mockito.*;
//
//class JobServiceImplTest {
//
//    @InjectMocks
//    private JobServiceImpl jobService;
//
//    @Mock
//    private JobRepository jobRepository;
//    @Mock
//    private NotificationService notificationService;
//    @Mock
//    private JavaMailSender mailSender;
//    @Mock
//    private UserRepository userRepository;
//    @Mock
//    private ResumeParser resumeParser;
//    @Mock
//    private PdfGeneratorService pdfGeneratorService;
//    @Mock
//    private JobMapper jobMapper;
//    @Mock
//    private ApplicantMapper applicantMapper;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//    }
//
//    @Test
//    void postJob_shouldCreateNewJob() throws HiringWireException {
//        JobRequest request = new JobRequest();
//        request.setId(0L);
//        request.setCompany("Company");
//        request.setJobTitle("Title");
//        request.setPostedBy(1L);
//
//        Job job = new Job();
//        Job savedJob = new Job();
//        savedJob.setId(1L);
//        savedJob.setCompany("Company");
//        savedJob.setJobTitle("Title");
//        savedJob.setPostedBy(1L);
//
//        JobResponse response = new JobResponse();
//        response.setId(1L);
//        response.setCompany("Company");
//
//        when(jobMapper.toEntity(any())).thenReturn(job);
//        when(jobRepository.save(any())).thenReturn(savedJob);
//        when(jobMapper.toResponse(any())).thenReturn(response);
//
//        JobResponse result = jobService.postJob(request);
//
//        assertNotNull(result);
//        assertEquals("Company", result.getCompany());
//        verify(notificationService).sendNotification(any(NotificationDTO.class));
//    }
//
//    @Test
//    void getAllJobs_shouldReturnJobs() throws HiringWireException {
//        Job job = new Job();
//        List<Job> jobs = List.of(job);
//        JobResponse response = new JobResponse();
//
//        when(jobRepository.findAll()).thenReturn(jobs);
//        when(jobMapper.toResponse(any())).thenReturn(response);
//
//        List<JobResponse> result = jobService.getAllJobs();
//
//        assertEquals(1, result.size());
//    }
//
//    @Test
//    void getJob_shouldReturnSingleJob() throws HiringWireException {
//        Job job = new Job();
//        JobResponse response = new JobResponse();
//
//        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
//        when(jobMapper.toResponse(job)).thenReturn(response);
//
//        JobResponse result = jobService.getJob(1L);
//
//        assertNotNull(result);
//    }
//
//    @Test
//    void approveJob_shouldUpdateStatus() throws HiringWireException {
//        Job job = new Job();
//        job.setId(1L);
//        job.setPostedBy(1L);
//        job.setJobTitle("Title");
//
//        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
//
//        jobService.approveJob(1L);
//
//        assertEquals(JobStatus.ACTIVE, job.getJobStatus());
//        verify(notificationService).sendNotification(any(NotificationDTO.class));
//    }
//
//    @Test
//    void rejectJob_shouldUpdateStatus() throws HiringWireException {
//        Job job = new Job();
//        job.setId(1L);
//        job.setPostedBy(1L);
//        job.setJobTitle("Title");
//
//        when(jobRepository.findById(1L)).thenReturn(Optional.of(job));
//
//        jobService.rejectJob(1L);
//
//        assertEquals(JobStatus.REJECTED, job.getJobStatus());
//        verify(notificationService).sendNotification(any(NotificationDTO.class));
//    }
//}