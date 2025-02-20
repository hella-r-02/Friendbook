package com.src.book.presentation.author.main_page.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.src.book.domain.model.author.Author
import com.src.book.domain.usecase.author.GetAuthorUseCase
import com.src.book.domain.utils.BasicState
import kotlinx.coroutines.launch

class AuthorViewModel(private val getAuthorUseCase: GetAuthorUseCase) :
    ViewModel() {
    private val _mutableLiveDataAuthor =
        MutableLiveData<BasicState<Author>>(BasicState.DefaultState())
    private val _mutableLiveDataIsLoading = MutableLiveData(false)
    val liveDataAuthor get() = _mutableLiveDataAuthor
    val liveDataIsLoading get() = _mutableLiveDataIsLoading

    fun loadAuthorById(id: Long) {
        viewModelScope.launch {
            _mutableLiveDataIsLoading.value = true
            _mutableLiveDataAuthor.value=getAuthorUseCase.execute(id)
//            val state = getAuthorUseCase.execute(id)
//            if (state is BasicStateTemplate.ErrorState) {
//                _mutableLiveDataAuthor.value = AuthorState.ErrorState(null)
//            } else {
//                _mutableLiveDataAuthor.value = AuthorState.SuccessState((state as BasicState.SuccessStateWithResources<Author>).data)
//            }
            _mutableLiveDataIsLoading.value = false
        }
    }
}