package models.services

import javax.inject.Inject

import forms.auth.StudentSearchForm
import models.daos.StudentDAO
import models.tables.Student

class StudentServiceImpl @Inject() (studentDAO: StudentDAO) extends StudentService {

  override def retrieve(id: String) = studentDAO.find(id)

  override def save(student: Student) = studentDAO.save(student)

  override def find(page: Int = 1) = studentDAO.find(page)

  override def all() = studentDAO.all()

  override def edit(student: Student) = studentDAO.edit(student)

  override def delete(id: String) = studentDAO.delete(id)

  override def find(formData: StudentSearchForm.Data) = studentDAO.find(formData)

  override def count() = studentDAO.count()
}
