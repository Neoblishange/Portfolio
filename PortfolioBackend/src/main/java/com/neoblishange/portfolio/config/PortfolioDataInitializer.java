package com.neoblishange.portfolio.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.neoblishange.portfolio.entity.Category;
import com.neoblishange.portfolio.entity.Education;
import com.neoblishange.portfolio.entity.Experience;
import com.neoblishange.portfolio.entity.Interest;
import com.neoblishange.portfolio.entity.project.Project;
import com.neoblishange.portfolio.entity.Skill;
import com.neoblishange.portfolio.entity.profile.Profile;
import com.neoblishange.portfolio.exception.ResourceNotFoundException;
import com.neoblishange.portfolio.repository.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class PortfolioDataInitializer implements CommandLineRunner {

    private final CategoryRepository categoryRepository;
    private final ExperienceRepository experienceRepository;
    private final EducationRepository educationRepository;
    private final InterestRepository interestRepository;
    private final ProjectRepository projectRepository;
    private final ProfileRepository profileRepository;
    private final SkillRepository skillRepository;
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
            SkillRepository skillRepository,
            ObjectMapper objectMapper
    ) {
        this.categoryRepository = categoryRepository;
        this.experienceRepository = experienceRepository;
        this.educationRepository = educationRepository;
        this.interestRepository = interestRepository;
        this.projectRepository = projectRepository;
        this.profileRepository = profileRepository;
        this.skillRepository = skillRepository;
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
                skill.setFeatured(skillData.featured());

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

            interest.setName(dataItem.name());
            interest.setDescription(dataItem.description());

            interestRepository.save(interest);
        }
    }

    private void importProjects(PortfolioData data) {
        for (PortfolioData.ProjectData dataItem : data.projects()) {
            Project project = new Project();

            project.setTitle(dataItem.title());
            project.setSlug(dataItem.slug());
            project.setProjectType(dataItem.projectType());
            project.setProjectContext(dataItem.projectContext());
            project.setShortDescription(dataItem.shortDescription());
            project.setDescription(dataItem.description());
            project.setFunctionalities(dataItem.functionalities());
            project.setStartDate(dataItem.startDate());
            project.setEndDate(dataItem.endDate());

            List<Skill> foundSkills =
                    skillRepository.findByNameIn(dataItem.skills());

            Map<String, Skill> skillsByName = new HashMap<>();
            for (Skill skill : foundSkills) {
                if (skillsByName.putIfAbsent(skill.getName(), skill) != null) {
                    throw new IllegalStateException(
                            "Duplicate skill name: " + skill.getName()
                    );
                }
            }

            List<Skill> projectSkills = dataItem.skills().stream()
                    .map(name -> {
                        Skill skill = skillsByName.get(name);
                        if (skill == null) {
                            throw new ResourceNotFoundException(
                                    "Skill \"" + name + "\" referenced by project \""
                                            + dataItem.title() + "\" was not found"
                            );
                        }
                        return skill;
                    })
                    .toList();

            project.setSkills(projectSkills);
            projectRepository.save(project);
        }
    }
}