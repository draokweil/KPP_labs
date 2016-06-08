package com.draokweil.game

import java.io.PrintWriter
import java.io.PrintWriter

import scala.io.Source
import scala.collection.JavaConversions._
import java.io.{PrintWriter, FileInputStream, ObjectInputStream, File}
import java.util

import scala.annotation.tailrec

object ScalaStats {
  
  
  
 val filename = "notation\\note"
  
 def getStats(): String = { 
    val filename = "statistics\\stats"
    try {
      val line = Source.fromFile(filename).getLines().toList
      line(1)
    } catch {
    case ex: Exception => println("Bummer, an exception happened.")
    return " "
    }
 }
 
  def getNotation(inp: String) {
    val list = Source.fromFile(inp).getLines().toList
     for( i <- 1 to list.size ) {
       compare(list(i))
     }
  }
  
  def compare(comp : String){
    comp match{
      case "Bot"=> writeLine("Режим_бота",filename)
      case "Player"=> writeLine("Режим_игрока",filename)
      case "m"=> writeLine("Движение(когда,куда): ",filename)
      case "pShot"=> writeLine("Выстрел_игрока(когда,откуда): ",filename)
      case "iShot"=> writeLine("Выстрел_противника(когда,откуда): ",filename)
      case "gg"=> writeLine("Конец_игры",filename)
      case _ => writeLine(comp,filename)
    }
  }
  
  private def writeLine(s: String, path: String) {
    var writer: java.io.BufferedWriter = null;
    try {
      writer = new java.io.BufferedWriter(new java.io.FileWriter(path, true));
      writer.write(s);
      writer.newLine();
      writer.flush();
    } catch {
      case e: java.io.IOException => println(e)
    } finally {
      if (writer != null) {
        try {
          writer.close();
        } catch {
          case e: java.io.IOException => println(e)
        }
      }
    }
  }
  
  
}