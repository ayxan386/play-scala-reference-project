package services.impl

import java.io.{BufferedReader, File, FileReader}
import java.nio.file.Paths
import java.util.stream.Collectors.joining

import com.google.common.io.BaseEncoding
import dto.SimpleResponseDTO
import javax.inject.{Inject, Singleton}
import models.ImageDM
import play.api.libs.Files.TemporaryFile
import play.api.mvc.MultipartFormData.FilePart
import repository.ImageRepository
import services.ImageService

import scala.concurrent.{ExecutionContext, Future}

@Singleton
class ImageServiceImpl @Inject()(imageRepository: ImageRepository)(
    implicit ex: ExecutionContext)
    extends ImageService {

  private val resourcePath = "./tmp/local/images"

  override def saveImageDirect(
      f: FilePart[TemporaryFile]): Future[SimpleResponseDTO] = {
    f.ref.copyTo(Paths.get(s"$resourcePath/${f.filename}"), replace = true)
    Future.successful(SimpleResponseDTO(message = "success"))
  }

  override def getByFilename(filename: String): Future[ImageDM] = {
    imageRepository
      .getByName(filename)
      .map(op => op.getOrElse(throw FileNotFoundError()))
      .map(dm =>
        dm.copy(content = new String(BaseEncoding.base64().decode(dm.content))))
  }

  override def getFileByName(filename: String): File = {
    Paths.get(s"$resourcePath/$filename").toFile
  }

  override def saveImage(
      f: FilePart[TemporaryFile]): Future[SimpleResponseDTO] = {
    val string = BaseEncoding
      .base64()
      .encode(try {
        new BufferedReader(new FileReader(f.ref))
          .lines()
          .collect(joining("\n"))
          .getBytes()
      } catch {
        case e: Exception => throw FileNotFoundError()
      })
    imageRepository
      .save(
        ImageDM(
          id = -1L,
          filename = Paths.get(f.filename).getFileName.toString,
          content = string,
          size = f.fileSize.toInt,
          fileType = f.contentType.getOrElse("all")
        ))
      .map(_.id)
      .map(id => SimpleResponseDTO(message = "success"))
  }
}
