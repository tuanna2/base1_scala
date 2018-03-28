package models.services

import javax.inject.Inject

import forms.auth.TermSearchForm
import models.daos.TermDAO
import models.tables.Term

class TermServiceImpl @Inject() (termDAO: TermDAO) extends TermService {

  override def retrieve(code: String) = termDAO.find(code)

  override def find(page: Int = 1) = termDAO.find(page)

  override def save(term: Term) = termDAO.save(term)

  override def all() = termDAO.all()

  override def edit(term: Term) = termDAO.edit(term)

  override def delete(code: String) = termDAO.delete(code)

  override def find(formData: TermSearchForm.Data) = termDAO.find(formData)

  override def count() = termDAO.count()
}
