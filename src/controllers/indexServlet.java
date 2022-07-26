package controllers;

import java.io.IOException;
import java.util.List;

import javax.persistence.EntityManager;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import models.Message;
import utils.DBUtil;

@WebServlet("/index")
//    Indexのサーブレット URLのプロジェクト名の後に/indexをつけたらこのページに飛ぶ
public class indexServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    public indexServlet() {
        super();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        EntityManager em = DBUtil.createEntityManager();

//        ↓のcreateNameQueryメソッドの引数にMessage.javaのJPQLの名前（getAllMessage)を入れるとデータベースへの問い合わせを実行できる。
        List<Message> messages = em.createNamedQuery("getAllMessages", Message.class).getResultList();

        em.close();

//        問い合わせ結果をリクエストスコープにセットする
        request.setAttribute("messages", messages);

     // フラッシュメッセージがセッションスコープにセットされていたら
        // リクエストスコープに保存する（セッションスコープからは削除）
        if(request.getSession().getAttribute("flush") != null) {
            request.setAttribute("flush", request.getSession().getAttribute("flush"));
            request.getSession().removeAttribute("flush");
        }

//        ↓のリクエストスコープ？でデータベースから取得したmessagesをセットし、index.jspを呼び出している
        RequestDispatcher rd = request.getRequestDispatcher("/WEB-INF/views/messages/index.jsp");
        rd.forward(request, response);
        //        ↓はデータの登録件数を表示するもの。
//        response.getWriter().append(Integer.valueOf(messages.size()).toString());

//        em.close();
    }

}
