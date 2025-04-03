# HiringWire – A Web-Based Job Portal System

## Overview
**HiringWire** is a web-based job portal system designed to efficiently connect job seekers with employers. This platform allows job seekers to register, create profiles, upload resumes, search job listings, and apply for positions. Employers can post job openings, review applications, and update statuses, while admins manage users and job listings.

## Features
- **User Authentication**: Secure authentication using **Spring Security with JWT** to manage different user roles (Applicants, Employers, Admins).
- **Job Listings**: Searchable job listings with filters (location, category) and pagination for efficient browsing.
- **Resume Management**: Resume uploads with validation (PDF format, max 5MB).
- **Application Tracking**: Employers can update application statuses (Applied, Accepted, Rejected).
- **Email Notifications**: Sends email notifications for application submissions and status updates using **JavaMailSender**.
- **Admin Panel**: Admins can manage users and job postings.

## Technologies Used
- **Backend**: Java Spring Boot
- **Frontend**: React
- **Database**: MySQL with Spring Data JPA & Hibernate
- **Authentication**: Spring Security & JWT
- **File Upload Handling**: Spring Multipart File Support
- **Email Service**: JavaMailSender with Gmail SMTP
