package com.tutran.utilities.file

import org.specs2.mock.Mockito
import org.specs2.mutable.Specification

class CSVFileUtilityTest extends Specification with Mockito {
  val util = new CSVFileUtility
  val resourcePath = "file/csv.file"


  case class TestUser(nodeId: String, qnameId: String, username: String, hashedPassword: String)

  "firstNLines" should {
    "successfully append 2 lines" in {
      val inStream = getClass.getClassLoader.getResourceAsStream("file/csv.file")
      val appendable = mock[Appendable]
      util.firstNLines(appendable)(inStream,2)
      there was one(appendable).println(anyString)
    }
    "successfully append all lines (6 lines)" in {
      val inStream = getClass.getClassLoader.getResourceAsStream("file/csv.file")
      val appendable = mock[Appendable]
      util.firstNLines(appendable)(inStream)
      there was 5.times(appendable).println(anyString)
    }
  }

  "write" should {
    val list = List(
      List.fill(3)("row1"),
      List.fill(3)("row2"),
      List.fill(3)("row3"))
    "successfully append all 3 lines" in {
      val appendable = mock[Appendable]
      util.write(appendable)(list)
      there was two(appendable).println(anyString)
    }
  }

  "toList" should {
    "return 2 users" in {
      val inStream = getClass.getClassLoader.getResourceAsStream("file/csv.file")
      val users = util.toList(classOf[TestUser])(inStream,2)
      users must have size 2
    }
    "return all 6 users" in {
      val inStream = getClass.getClassLoader.getResourceAsStream("file/csv.file")
      val users = util.toList(classOf[TestUser])(inStream)
      users must have size 6

    }
    "first user must match (nodeId=11117,qnameId=11,username=000331@jdpafncontactcenteruser.com)" in {
      val inStream = getClass.getClassLoader.getResourceAsStream("file/csv.file")
      val user = util.toList(classOf[TestUser])(inStream,1).head
      user.username mustEqual "000331@jdpafncontactcenteruser.com"
      user.nodeId mustEqual "11117"
      user.qnameId mustEqual "11"
    }
  }

  "parse" should {
    "return 2 rows" in {
      val inStream = getClass.getClassLoader.getResourceAsStream("file/csv.file")
      val rows = util.parse(inStream,2)
      rows must have size 2
    }
    "return all 6 rows" in {
      val inStream = getClass.getClassLoader.getResourceAsStream("file/csv.file")
      val rows = util.parse(inStream)
      rows must have size 6
    }
  }

  "toFile" should {
    val users = List(
      TestUser("nodeid1","qId1","usrname1","password1"),
      TestUser("nodeid2","qId2","usrname2","password2"),
      TestUser("nodeid3","qId3","usrname3","password3"))

    "append 3 users and 1 header" in {
      val appendable = mock[Appendable]
      util.toFile(appendable)(users)
      there was 4.times(appendable).println(anyString)
    }
  }
}
