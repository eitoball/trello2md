import okhttp3.mockwebserver.MockResponse
import org.assertj.core.api.Assertions.assertThat
import org.junit.Test
import okhttp3.mockwebserver.MockWebServer

class TrelloTest {
    @Test
    fun getComments() {
        val trello = Trello("", "")
        val server = MockWebServer()
        trello.baseUrl = server.url("/").toString()

        val json = """
[
  {
   "id":"1000",
   "data": {
       "list":{"name":"aList","id":"2000"},
       "board":{"shortLink":"linkToBoard","name":"nameOfBoard","id":"3000"},
       "card":{"shortLink":"linkToCard","idShort":1,"name":"nameOfCard","id":"4000"},
       "text":"hoge hoge fuga fuga"
   },
   "date":"2017-03-29T07:46:13.073Z",
   "memberCreator":{"id":"5000","avatarHash":"hashToAvatar","fullName":"fullName","initials":"YK","username":"yusuke kokubo"}
  }
]
"""
        server.enqueue(MockResponse().setBody(json))

        val comments = trello.getComments("")

        assertThat(comments.size).isEqualTo(1)
        assertThat(comments[0].id).isEqualTo("1000")
        assertThat(comments[0].data.text).isEqualTo("hoge hoge fuga fuga")
        assertThat(comments[0].date).isEqualTo("2017-03-29T07:46:13.073Z")

        server.shutdown()
    }

    @Test
    fun getAttachments() {
        val trello = Trello("", "")
        val server = MockWebServer()
        trello.baseUrl = server.url("/").toString()

        val json = """
[
  {
   "id":"0",
   "data":{
     "board":{"shortLink":"link","name":"example","id":"1"},
     "list":{"name":"Hoge","id":"2"},
     "card":{"shortLink":"cardToLink","idShort":3,"name":"晴天なり","id":"3"},
     "attachment":{
       "url":"url",
       "name":"hoge",
       "id":"4",
       "previewUrl":"previewUrl",
       "previewUrl2x":"previewUrl2"
     }
   },
   "date":"2017-03-31T13:55:48.139Z",
   "memberCreator":{"id":"5","avatarHash":"avatarHash","fullName":"Yusuke Kokubo","initials":"YK","username":"ykokubo"}}]
"""

        server.enqueue(MockResponse().setBody(json))

        val comments = trello.getComments("")

        assertThat(comments.size).isEqualTo(1)
        assertThat(comments[0].data.attachment!!.url).isEqualTo("url")

        server.shutdown()
    }
}
