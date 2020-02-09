package ru.cft.focusstart.entity.types;

public class HibernateInstructorCategoryUserType extends PGEnumUserType<InstructorCategory> {
    public HibernateInstructorCategoryUserType() {
        super(InstructorCategory.class);
    }
}
