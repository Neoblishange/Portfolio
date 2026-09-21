package com.neoblishange.portfolio.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.neoblishange.portfolio.config.data.PortfolioData;
import com.neoblishange.portfolio.entity.Category;
import com.neoblishange.portfolio.entity.Education;
import com.neoblishange.portfolio.entity.Experience;
import com.neoblishange.portfolio.entity.Interest;
import com.neoblishange.portfolio.entity.Project;
import com.neoblishange.portfolio.entity.Skill;
import com.neoblishange.portfolio.entity.profile.Profile;
import com.neoblishange.portfolio.repository.CategoryRepository;
import com.neoblishange.portfolio.repository.EducationRepository;
import com.neoblishange.portfolio.repository.ExperienceRepository;
import com.neoblishange.portfolio.repository.InterestRepository;
import com.neoblishange.portfolio.repository.ProfileRepository;
import com.neoblishange.portfolio.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;

@Component
public class PortfolioDataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ExperienceRepository experienceRepository;
    private final EducationRepository educationRepository;
    private final InterestRepository interestRepository;
    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;
    private final ObjectMapper objectMapper;

    @Value("${app.data.file:/app/data/portfolio-data.json}")
    private String dataFile;

    public PortfolioDataInitializer(
            CategoryRepository categoryRepository,
            ExperienceRepository experienceRepository,
            EducationRepository educationRepository,
            InterestRepository interestRepository,
            ProjectRepository projectRepository,
            ProfileRepository profileRepository,
            ObjectMapper objectMapper
    ) {
        this.categoryRepository = categoryRepository;
        this.experienceRepository = experienceRepository;
        this.educationRepository = educationRepository;
        this.interestRepository = interestRepository;
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
        this.objectMapper = objectMapper;
    }

    @Override
    @Transactional
    public void run(String... args) throws Exception {

        if (hasPortfolioData()) {
            return;
        }

        PortfolioData data;

        try (InputStream inputStream =
                     new java.io.FileInputStream(dataFile)) {

            data = objectMapper.readValue(inputStream, PortfolioData.class);
        }

        importProfile(data.profile());
        importCategories(data);
        importExperiences(data);
        importEducations(data);
        importInterests(data);
        importProjects(data);
    }

    private boolean hasPortfolioData() {
        return profileRepository.count() > 0
                || categoryRepository.count() > 0
                || experienceRepository.count() > 0
                || educationRepository.count() > 0
                || interestRepository.count() > 0
                || projectRepository.count() > 0;
    }

    private void importProfile(PortfolioData.ProfileData data) {

        Profile profile = new Profile();

        profile.setFirstName(data.firstName());
        profile.setLastName(data.lastName());
        profile.setJobTitle(data.jobTitle());
        profile.setPhone(data.phone());
        profile.setEmail(data.email());
        profile.setLinkedinUrl(data.linkedinUrl());
        profile.setGithubUrl(data.githubUrl());
        profile.setPitch(data.pitch());
        profile.setYearsOfExperience(data.yearsOfExperience());
        profile.setAvailability(data.availability());

        profileRepository.save(profile);
    }

    private void importCategories(PortfolioData data) {

        for (PortfolioData.CategoryData categoryData : data.categories()) {

            Category category = new Category();

            category.setName(categoryData.name());

            for (PortfolioData.SkillData skillData : categoryData.skills()) {

                Skill skill = new Skill();

                skill.setName(skillData.name());
                skill.setDisplayOrder(skillData.displayOrder());
                skill.setLevel(skillData.level());
                skill.setCategory(category);

                category.getSkills().add(skill);
            }

            categoryRepository.save(category);
        }
    }

    private void importExperiences(PortfolioData data) {

        for (PortfolioData.ExperienceData dataItem : data.experiences()) {

            Experience experience = new Experience();

            experience.setCompany(dataItem.company());
            experience.setPosition(dataItem.position());
            experience.setLocation(dataItem.location());
            experience.setStartDate(dataItem.startDate());
            experience.setEndDate(dataItem.endDate());
            experience.setCurrent(dataItem.current());
            experience.setDescription(dataItem.description());

            experienceRepository.save(experience);
        }
    }

    private void importEducations(PortfolioData data) {

        for (PortfolioData.EducationData dataItem : data.educations()) {

            Education education = new Education();

            education.setSchoolName(dataItem.schoolName());
            education.setLocation(dataItem.location());
            education.setStartDate(dataItem.startDate());
            education.setEndDate(dataItem.endDate());
            education.setDegree(dataItem.degree());
            education.setDescription(dataItem.description());

            educationRepository.save(education);
        }
    }

    private void importInterests(PortfolioData data) {

        for (PortfolioData.InterestData dataItem : data.interests()) {

            Interest interest = new Interest();

            interest.setDescription(dataItem.description());

            interestRepository.save(interest);
        }
    }

    private void importProjects(PortfolioData data) {

        for (PortfolioData.ProjectData dataItem : data.projects()) {

            Project project = new Project();

            project.setTitle(dataItem.title());
            project.setSlug(dataItem.slug());
            project.setShortDescription(dataItem.shortDescription());
            project.setDescription(dataItem.description());
            project.setStartDate(dataItem.startDate());
            project.setEndDate(dataItem.endDate());

            projectRepository.save(project);
        }
    }
}