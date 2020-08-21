package services.impl

import java.io.{BufferedReader, File, FileReader}
import java.nio.charset.Charset
import java.nio.file.{Files, Paths}
import java.util.stream.Collectors.joining

import com.google.common.io.BaseEncoding
import dto.SimpleResponseDTO
import javax.inject.{Inject, Singleton}
import models.ImageDM
import play.api.libs.Files.TemporaryFile
import play.api.libs.Files.TemporaryFile.{
  temporaryFileToFile,
  temporaryFileToPath
}
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

  override def getByFilename(filename: String): Future[ImageDM] =
    imageRepository
      .getByName(filename)
      .map(op => op.getOrElse(throw FileNotFoundError()))

  override def getFileByName(filename: String): File = {
    Paths.get(s"$resourcePath/$filename").toFile
  }

  override def saveImage(
      f: FilePart[TemporaryFile]): Future[SimpleResponseDTO] = {
    imageRepository
      .save(
        ImageDM(
          id = -1L,
          filename = Paths.get(f.filename).getFileName.toString,
          content = Files.readAllBytes(temporaryFileToPath(f.ref)),
          size = f.fileSize.toInt,
          fileType = f.contentType.getOrElse("all")
        ))
      .map(_.id)
      .map(id => SimpleResponseDTO(message = "success"))
  }
}
