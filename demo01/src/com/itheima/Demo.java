package com.itheima;

import com.sun.glass.ui.Size;
import org.apache.commons.io.FileUtils;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.document.TextField;
import org.apache.lucene.index.*;
import org.apache.lucene.search.*;
import org.apache.lucene.store.Directory;
import org.apache.lucene.store.FSDirectory;
import org.apache.lucene.util.packed.DirectReader;

import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public class Demo {
    //===========创建索引库的步骤================
    //1.创建一个directory目录对象,指定索引库保存的位置
    //2.基于directory对象创建保存数据的的对象IndexWriter索引对象
    //3.读取磁盘上的文件,对应每个文件创建一个文档对象
    //4.向文档对象中添加域
    //5.把文档对象写入索引库
    //6.关闭索引对象
    //备注:将文件信息加入域对象中,将域对象加入文档对象中,将文档对象加入写索引对象中
    //===========查询索引库的步骤================
    //1.创建一个directory对象,指定索引库的位置
    //2.创建一个indexreader对象
    //3.创建一个indexsearcher对象,构造方法中的参数indexreader对象
    //4.创建一个query对象,termquery
    //5.执行查询,得到一个topdocs对象
    //6.取查询结果的总的记录数
    //7.取文档列表
    //8.打印文档中的内容
    //9.关闭indexreader对象
    //备注:创建读索引对象,通过读索引对象创建索引搜索对象,创建query对象,索引搜索对象通过query对象获得topdocs对象
    //==============美丽的分割线==================

    String database = "C:\\Users\\江小白\\Desktop\\index\\data";
    String fileDir = "C:\\Users\\江小白\\Desktop\\index\\source";

    public void create() throws IOException {
        //1.创建一个directory目录对象,指定索引库保存的位置
        Directory directory = FSDirectory.open(new File(database).toPath());
        //2.基于directory对象创建保存数据的的对象IndexWriter索引对象
        IndexWriter iw = new IndexWriter(directory, new IndexWriterConfig());
        //3.读取磁盘上的文件,对应每个文件创建一个文档对象
        File file = new File(fileDir);
        File[] files = file.listFiles();
        //4.向文档对象中添加域
        for (File file1 : files) {
            //读取文件名
            String name = file1.getName();
            //文件的路径
            String file1Path = file1.getPath();
            //读取文件内容
            String content = FileUtils.readFileToString(file1, "utf-8");
            //文件的大小
            long fileSize = FileUtils.sizeOf(file1);
            //将上述信息存入到域对象中
            Field fname = new TextField("name",name, Field.Store.YES);
            Field fpath = new TextField("path", file1Path, Field.Store.YES);
            Field fcontent = new TextField("content",content, Field.Store.YES);
            Field fsize = new TextField("size", fileSize + "", Field.Store.YES);
            //创建文档对象
            Document document = new Document();
            //4向文档对象中添加域
            document.add(fname);
            document.add(fpath);
            document.add(fcontent);
            document.add(fsize);
            //5.把文档对象写入索引库
            iw.addDocument(document);
        }
        //6.关闭索引对象
        iw.close();
    }

    public void searchIndex() throws Exception {
        //1.创建一个directory对象,指定索引库的位置
        Directory directory = FSDirectory.open(new File(database).toPath());
        //2.创建一个indexreader对象
        IndexReader ir = DirectoryReader.open(directory);
        //3.创建一个indexsearcher对象,构造方法中的参数indexreader对象
        IndexSearcher is = new IndexSearcher(ir);
        //4.创建一个query对象,termquery
        Query query = new TermQuery(new Term("content","spring"));
        //5.执行查询,得到一个topdocs对象
        TopDocs topDocs = is.search(query, 10);
        //6.取查询结果的总的记录数
        System.out.println("查询总记录数:" + topDocs.totalHits );
        //7.取文档列表
        ScoreDoc[] scoreDocs = topDocs.scoreDocs;
        //8.打印文档中的内容
        for (ScoreDoc scoreDoc : scoreDocs) {
            //取文档id
            int docid = scoreDoc.doc;
            //根据id获取文档对象
            Document doc = is.doc(docid);
            String name = doc.get("name");
            String path = doc.get("path");
            String content = doc.get("content");
            String size = doc.get("size");
            System.out.println("name = " + name);
            System.out.println("path = " + path);
            System.out.println("size = " + size);
        }
        //9.关闭indexreader对象
        ir.close();
    }
    public static void main(String[] args) throws Exception {
//        new Demo().create();
        new Demo().searchIndex();
    }
}











































































