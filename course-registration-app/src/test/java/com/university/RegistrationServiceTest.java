package com.university;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RegistrationServiceTest {

    @Test
    void shouldRegisterStudentForValidCourse() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS301"));
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectInactiveCourse() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                false,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS301"));
    }

    @Test
    void shouldRejectMissingPrerequisite() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS401",
                "Advanced Algorithms",
                4,
                30,
                true,
                "CS301"
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertFalse(result);
        assertFalse(student.isRegistered("CS401"));
    }

    @Test
    void shouldAllowRegistrationWhenPrerequisiteIsCompleted() {

        Student student = new Student("S101", "Anita");
        student.addCompletedPrerequisite("CS301");

        Course course = new Course(
                "CS401",
                "Advanced Algorithms",
                4,
                30,
                true,
                "CS301"
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertTrue(student.isRegistered("CS401"));
    }

    @Test
    void shouldRejectDuplicateRegistration() {

        Student student = new Student("S101", "Anita");

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        assertTrue(service.register(student, course));

        boolean secondAttempt = service.register(student, course);

        assertFalse(secondAttempt);
        assertEquals(4, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    // @Test
    // void shouldRejectRegistrationWhenCreditLimitIsExceeded() {

    //     Student student = new Student("S101", "Anita");

    //     // Simulate 20 existing credits
    //     student.addRegistration("CS101", 4);
    //     student.addRegistration("CS102", 4);
    //     student.addRegistration("CS201", 4);
    //     student.addRegistration("CS202", 4);
    //     student.addRegistration("CS203", 4);

    //     Course course = new Course(
    //             "CS301",
    //             "Data Structures",
    //             4,
    //             30,
    //             true,
    //             null
    //     );

    //     RegistrationService service = new RegistrationService();

    //     boolean result = service.register(student, course);

    //     assertFalse(result);
    //     assertFalse(student.isRegistered("CS301"));
    // }

    @Test
    void shouldAllowRegistrationWhenTotalCreditsReach24() {

        Student student = new Student("S104", "Arun");

        // Simulate 20 existing credits.
        student.addRegistration("CS101", 4);
        student.addRegistration("CS102", 4);
        student.addRegistration("CS201", 4);
        student.addRegistration("CS202", 4);
        student.addRegistration("CS203", 4);

        Course course = new Course(
                "CS301",
                "Data Structures",
                4,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        boolean result = service.register(student, course);

        // Registration should succeed because the total becomes exactly 24.
        assertTrue(result);
        assertTrue(student.isRegistered("CS301"));
        assertEquals(24, student.getTotalCredits());
        assertEquals(1, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectRegistrationWhenTotalCreditsWouldExceed24() {

        Student student = new Student("S105", "Meera");

        // Simulate exactly 24 existing credits.
        student.addRegistration("CS101", 4);
        student.addRegistration("CS102", 4);
        student.addRegistration("CS201", 4);
        student.addRegistration("CS202", 4);
        student.addRegistration("CS203", 4);
        student.addRegistration("CS204", 4);

        Course course = new Course(
                "CS302",
                "Operating Systems",
                1,
                30,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        // Record the initial state before attempting registration.
        int initialCredits = student.getTotalCredits();
        int initialRegisteredCourses = student.getRegisteredCourses().size();
        int initialEnrollment = course.getCurrentEnrollment();

        boolean result = service.register(student, course);

        // Registration must be rejected because the total would become 25.
        assertFalse(result);

        // Verify the student's state remains unchanged.
        assertEquals(initialCredits, student.getTotalCredits());
        assertEquals(
                initialRegisteredCourses,
                student.getRegisteredCourses().size()
        );
        assertFalse(student.isRegistered("CS302"));

        // Verify the course's enrollment remains unchanged.
        assertEquals(initialEnrollment, course.getCurrentEnrollment());
    }

    @Test
    void shouldRejectRegistrationWhenCourseIsFull() {

        Student student = new Student("S102", "Rahul");

        Course course = new Course(
                "CS302",
                "Operating Systems",
                4,
                2,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        // Fill the course to its capacity.
        course.incrementEnrollment();
        course.incrementEnrollment();

        // Record the student's initial state.
        int initialCredits = student.getTotalCredits();
        int initialEnrollment = course.getCurrentEnrollment();
        int initialRegisteredCourses = student.getRegisteredCourses().size();

        // Attempt registration when the course is already full.
        boolean result = service.register(student, course);

        // Registration must be rejected.
        assertFalse(result);

        // Rejection must not modify the student's state.
        assertEquals(initialCredits, student.getTotalCredits());
        assertEquals(
                initialRegisteredCourses,
                student.getRegisteredCourses().size()
        );
        assertFalse(student.isRegistered("CS302"));

        // Rejection must not modify course enrollment.
        assertEquals(initialEnrollment, course.getCurrentEnrollment());
    }

    @Test
    void shouldAllowRegistrationWhenOneSeatRemains() {
        Student student = new Student("S103", "Priya");

        Course course = new Course(
                "CS303",
                "Database Systems",
                4,
                2,
                true,
                null
        );

        RegistrationService service = new RegistrationService();

        // Fill all but one seat.
        course.incrementEnrollment();

        int initialCredits = student.getTotalCredits();
        int initialRegisteredCourses = student.getRegisteredCourses().size();
        int initialEnrollment = course.getCurrentEnrollment();

        boolean result = service.register(student, course);

        assertTrue(result);
        assertEquals(initialCredits + 4, student.getTotalCredits());
        assertEquals(initialRegisteredCourses + 1, student.getRegisteredCourses().size());
        assertTrue(student.isRegistered("CS303"));
        assertEquals(initialEnrollment + 1, course.getCurrentEnrollment());
    }
}