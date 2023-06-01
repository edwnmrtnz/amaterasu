
package com.github.amaterasu.localtest

import com.google.common.truth.Truth.*
import org.junit.Test
import org.mockito.Mockito


class MoviesTest {

    data class Movie(val id: String, val name: String)

    interface MoviesRepository {
        fun fetch(params: Map<String, String>): List<Movie>
    }

    class FetchMoviesUseCase(private val repository: MoviesRepository) {
        fun execute(page: Int): List<Movie> {
            return repository.fetch(mapOf("page" to page.toString(), "status" to "active"))
        }
    }

    @Test
    fun `should only fetch active movies`() {
        val repo = Mockito.mock(MoviesRepository::class.java)
        val sut = FetchMoviesUseCase(repo)

        sut.execute(page = 1)

        val captor = com.github.amaterasu.localtest.argumentCaptor<Map<String, String>>()
        Mockito.verify(repo).fetch(com.github.amaterasu.localtest.capture(captor))
        val params = captor.value
        assertThat(params["status"]).isEqualTo("active")
    }


}
