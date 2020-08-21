package services

import java.io.File

import com.google.inject.ImplementedBy
import dto.SimpleResponseDTO
import models.ImageDM
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart
import services.impl.ImageServiceImpl

import scala.concurrent.Future
import scala.io.Source

@ImplementedBy(classOf[ImageServiceImpl])
trait ImageService {

  def saveImage(f: FilePart[TemporaryFile]): Future[SimpleResponseDTO]

  def getByFilename(filename: String): Future[ImageDM]

}
