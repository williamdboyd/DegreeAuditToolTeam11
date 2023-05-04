package objects;

import java.util.List;

public class DegreePlan {
	protected List<Course> coreCourses;
	protected List<Course> coreCourseOptions;
	protected List<Course> admissionPrerequisites;
	
	public DegreePlan() {
		coreCourses = null;
		coreCourseOptions = null;
		admissionPrerequisites = null;
	}
	
	public List<Course> getCoreCourses() {
		return this.coreCourses;
	}
	
	public List<Course> getCoreOptions() {
		return this.coreCourseOptions;
	}
	
	public List<Course> getAdmissionPreReq() {
		return this.admissionPrerequisites;
	}
	
	
}
