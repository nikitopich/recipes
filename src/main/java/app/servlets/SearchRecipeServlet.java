package app.servlets;

import org.mozilla.universalchardet.UniversalDetector;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.ArrayList;

public class SearchRecipeServlet extends HttpServlet {

    public final String path = "C:\\Users\\nec99\\IdeaProjects\\recipes\\src\\main\\resources";

    public static ArrayList<String> recipeText = new ArrayList<String>();
    public static String searchWord;
    public static ArrayList<File> fileArrayList = new ArrayList<File>();

    public static String readUsingBufferedReader(String fileName) throws IOException {      //Помещение текстового файла в строку
        BufferedReader reader = new BufferedReader(new FileReader(fileName));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");
        while ((line = reader.readLine()) != null) {
            stringBuilder.append("<p>" + line + "</p>");
            stringBuilder.append(ls);
        }

        stringBuilder.deleteCharAt(stringBuilder.length() - 1);
        return stringBuilder.toString();
    }

    public static void listFilesForFolder(final File folder) {       //Лист содержащий ссылки на файлы
        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isDirectory()) {
                listFilesForFolder(fileEntry);
            } else {
                fileArrayList.add(fileEntry);
            }
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        RequestDispatcher requestDispatcher = req.getRequestDispatcher("views/search.jsp");
        requestDispatcher.forward(req, resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("text/html; charset=UTF-8");
        PrintWriter out = resp.getWriter();
        req.setCharacterEncoding("UTF-8");


        /*---------------Считываем слово--------------------*/
        searchWord = req.getParameter("forsearch");
        out.println(searchWord);
        /*--------------------------------------------------*/


        /*---------------Добавляем файлы с рецептами--------*/
        final File folder = new File(path);
        listFilesForFolder(folder);
        out.print("<p>" + fileArrayList.size() + "</p>");
        /*--------------------------------------------------*/


        /*---------------Добавлем содержимое файлов---------*/
        for (File entryFile : fileArrayList) {
            recipeText.add(readUsingBufferedReader(entryFile.toString()));
        }
        /*--------------------------------------------------*/


        for (String str : recipeText) {
            if (str.toLowerCase().contains(searchWord.toLowerCase())) {
                System.out.println(str);
                out.println(str + "\n");
            }
        }
    }
}
