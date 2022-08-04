package com.berkeyoncaci.moviemanagement.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.berkeyoncaci.moviemanagement.exception.ResourceNotFoundException;
import com.berkeyoncaci.moviemanagement.model.Movie;
import com.berkeyoncaci.moviemanagement.repository.MovieRepository;

@RestController
@RequestMapping("/api/v1/")
public class MovieController {
	
	@Autowired
	private MovieRepository movieRepository;
	
	// Endpoint for getting all movies as a list
	@GetMapping("movies")
	public List<Movie> getAllMovies() {
		return this.movieRepository.findAll();
	}
	
	// Endpoint for getting a movie with an ID
	@GetMapping("movies/{id}")
	public ResponseEntity<Movie> getMovieById(@PathVariable(value = "id") Long movieId)
			throws ResourceNotFoundException {
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> new ResourceNotFoundException("Movie not found with the ID: " + movieId));
		
		return ResponseEntity.ok().body(movie);
	}
	
	// Endpoint for creating a movie
	@PostMapping("movies")
	public Movie createMovie(@RequestBody Movie movie) {
		return this.movieRepository.save(movie);
	}
	
	// Endpoint for updating an existing movie
	@PutMapping("movies/{id}")
	public ResponseEntity<Movie> updateMovie(@PathVariable(value = "id") Long movieId,
			@RequestBody Movie movieData) throws ResourceNotFoundException {
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> new ResourceNotFoundException("Movie not found with the ID: " + movieId));
		
		movie.setName(movieData.getName());
		movie.setReleaseDate(movieData.getReleaseDate());
		movie.setGenre(movieData.getGenre());
		
		return ResponseEntity.ok(this.movieRepository.save(movie));
	}
	
	// Endpoint for deleting a movie
	@DeleteMapping("movies/{id}")
	public Map<String, Boolean> deleteMovie(@PathVariable(value = "id") Long movieId)
			throws ResourceNotFoundException {
		Movie movie = movieRepository.findById(movieId)
				.orElseThrow(() -> new ResourceNotFoundException("Movie not found with the ID: " + movieId));
		
		this.movieRepository.delete(movie);
		
		Map<String, Boolean> response = new HashMap<>();
		response.put("deleted", Boolean.TRUE);
		
		return response;
	}
}
