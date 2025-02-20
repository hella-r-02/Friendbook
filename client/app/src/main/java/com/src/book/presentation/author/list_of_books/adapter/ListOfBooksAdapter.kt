package com.src.book.presentation.author.list_of_books.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.src.book.R
import com.src.book.databinding.ViewHolderBookBinding
import com.src.book.domain.model.book.BookList
import com.src.book.presentation.utils.RatingColor

//TODO Если нет рейтинга что делать
class ListOfBooksAdapter(
    private val onClickMore: ((it: BookList) -> Unit)? = null,
    private val onClickBook: (it: BookList) -> Unit,
    private val onCLickBookmark: ((it: BookList) -> Unit)? = null,

    ) :
    ListAdapter<BookList, ListOfBooksAdapter.DataViewHolder>(BookDiffCallback()) {
    private lateinit var binding: ViewHolderBookBinding

    class DataViewHolder(private val binding: ViewHolderBookBinding) :
        RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("SetTextI18n")
        fun onBind(
            book: BookList, onClickMore: ((it: BookList) -> Unit)?,
            onClickBook: (it: BookList) -> Unit,
            onCLickBookmark: ((it: BookList) -> Unit)?
        ) {
            Glide.with(context)
                .load(book.linkCover)
                .into(binding.ivBook)
            binding.tvBookName.text = book.name
            binding.tvBookAuthor.text = book.authors?.joinToString(", ") { it.name }
            with(binding.tvGlobalRating) {
                text = if (book.rating == null) {
                    context.resources.getText(R.string.no_rating)
                } else {
                    book.rating.toString()
                }
                setTextColor(ContextCompat.getColor(context, RatingColor.getColor(book.rating)))
            }
            //TODO проверить год на null
//            if (book.genres != null && book.genres.isNotEmpty()) {
////                binding.tvBookYearGenre.text =
////                    "${book.year}, " + book.genres.joinToString(", ") { it.name }
//            } else {
//                binding.tvBookYearGenre.text =
//                    book.year
//            }
            if (onClickMore != null) {
                binding.ivMore.setOnClickListener {
                    onClickMore(book)
                }
            } else {
                binding.ivMore.visibility = View.GONE
            }
            itemView.setOnClickListener {
                onClickBook(book)
            }
            if (onCLickBookmark != null) {
                binding.ivBookmark.setOnClickListener {
                    onCLickBookmark(book)
                    if (book.isAuth) {
                        setColorForBookMark(book, context)
                    }
                }
            }
            setColorForBookMark(book, context)
            if (book.isAuth) {
                if (book.grade != null) {
                    binding.tvRating.text = book.grade.toString()
                    binding.userRating.background = ContextCompat.getDrawable(
                        context,
                        RatingColor.getBackground(book.grade.toDouble())
                    )
                } else {
                    binding.userRating.visibility = View.GONE
                }

            } else {
                binding.userRating.visibility = View.GONE
            }
        }

        private fun setColorForBookMark(book: BookList, context: Context) {
            if (book.isWantToRead) {
                binding.ivBookmark.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.basic_color
                    )
                )
            } else {
                binding.ivBookmark.setColorFilter(
                    ContextCompat.getColor(
                        context,
                        R.color.icon_nav_not_selected
                    )
                )
            }
        }

        private val RecyclerView.ViewHolder.context get() = this.itemView.context
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DataViewHolder {
        binding = ViewHolderBookBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return DataViewHolder(binding)
    }

    override fun onBindViewHolder(holder: DataViewHolder, position: Int) {
        val item = getItem(position)
        holder.itemView.animation =
            AnimationUtils.loadAnimation(holder.itemView.context, R.anim.fade_in)
        holder.onBind(item, onClickMore, onClickBook, onCLickBookmark)
    }
}

class BookDiffCallback : DiffUtil.ItemCallback<BookList>() {
    override fun areItemsTheSame(oldItem: BookList, newItem: BookList): Boolean {
        return oldItem.id == newItem.id
    }

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: BookList, newItem: BookList): Boolean {
        return oldItem == newItem
    }
}