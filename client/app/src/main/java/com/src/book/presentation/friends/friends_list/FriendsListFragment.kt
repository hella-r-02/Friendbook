package com.src.book.presentation.friends.friends_list

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.src.book.R
import com.src.book.databinding.FragmentFriendsListBinding
import com.src.book.domain.model.Friend
import com.src.book.domain.utils.BasicState
import com.src.book.presentation.MainActivity
import com.src.book.presentation.friends.friends_list.adapter.FriendsAdapter
import com.src.book.presentation.friends.friends_list.viewModel.FriendsListViewModel
import com.src.book.presentation.friends.friends_requests.RequestsFriendsFragment
import com.src.book.utils.REGEX_SPACE
import java.util.*

class FriendsListFragment : Fragment() {
    private lateinit var binding: FragmentFriendsListBinding
    private lateinit var viewModel: FriendsListViewModel
    private lateinit var friendsList: ArrayList<Friend>
    private lateinit var fullFriendsList: ArrayList<Friend>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        this.binding = FragmentFriendsListBinding.inflate(inflater)
        viewModel = (activity as MainActivity).getFriendsListViewModel()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.liveDataFriends.observe(
            this.viewLifecycleOwner, this::checkLoadFriendsState
        )
        viewModel.liveDataIncomingRequestsCount.observe(
            this.viewLifecycleOwner, this::checkStateForIncomingRequests
        )
        viewModel.liveDataRemoveFriend.observe(
            this.viewLifecycleOwner, this::checkStateRemoveFriend
        )
        viewModel.loadIncomingRequestsCount()
        viewModel.loadFriends()
        binding.ivAddPicture.setOnClickListener {
            (activity as MainActivity).replaceFragment(RequestsFriendsFragment())
        }
        setTextChangeListenerForSearch()
        setOnClickListenerForTryAgainButton()
    }

    private fun setTextChangeListenerForSearch() {
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            @SuppressLint("NotifyDataSetChanged")
            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                val text = binding.etSearch.text.toString()
                    .replace(REGEX_SPACE, " ")
                    .lowercase(Locale.getDefault())
                    .trim()
                friendsList.clear()
                val newList: ArrayList<Friend> = ArrayList(friendsList.size)
                if (text.isNotEmpty()) {
                    fullFriendsList.forEach {
                        val nameLowerCase = it.name.lowercase(Locale.getDefault())
                        val loginLowerCase = it.login.lowercase(Locale.getDefault())
                        if (nameLowerCase.contains(text) || loginLowerCase.contains(text)) {
                            if (!newList.contains(it)) {
                                newList.add(it)
                            }
                        }
                    }
                    friendsList.addAll(newList)
                } else {
                    friendsList.addAll(fullFriendsList)
                }
                binding.rvFriends.adapter?.notifyDataSetChanged()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun checkLoadFriendsState(state: BasicState<List<Friend>>) {
        when (state) {
            is BasicState.SuccessState -> {
                setVisibilityForShimmer(View.INVISIBLE)
                loadData(state.data)
            }
            is BasicState.LoadingState -> {
                setVisibilityForShimmer(View.VISIBLE)
            }
            is BasicState.ErrorState -> {
                binding.llError.visibility = View.VISIBLE
                binding.rvFriends.visibility = View.GONE
                binding.tvEmptyListFriend.visibility = View.GONE
                setVisibilityForShimmer(View.INVISIBLE)
            }
            else -> {}
        }
    }

    private fun setVisibilityForShimmer(visibility: Int) {
        if (visibility == View.VISIBLE) {
            binding.slFriends.startShimmer()
        } else {
            binding.slFriends.stopShimmer()
            binding.slFriends.visibility = visibility
        }
    }

    @SuppressLint("SetTextI18n")
    private fun loadData(friends: List<Friend>) {
        binding.llError.visibility = View.GONE
        if (friends.isEmpty()) {
            binding.rvFriends.visibility = View.GONE
            binding.tvEmptyListFriend.visibility = View.VISIBLE
        } else {
            binding.rvFriends.visibility = View.VISIBLE
            binding.tvEmptyListFriend.visibility = View.GONE
            setAdapterForFriendsRecyclerView(friends)
        }
        binding.tvTitle.text = "${getString(R.string.friends_with_holder)} (${friends.size})"
    }

    private fun setAdapterForFriendsRecyclerView(friends: List<Friend>) {
        friendsList = ArrayList(friends)
        fullFriendsList = ArrayList(friends)
        val adapter = FriendsAdapter { id, position -> removeFriend(id, position) }
        adapter.submitList(friendsList)
        val layoutManager = GridLayoutManager(requireContext(), 1, RecyclerView.VERTICAL, false)
        binding.rvFriends.layoutManager = layoutManager
        binding.rvFriends.adapter = adapter
    }

    private fun checkStateForIncomingRequests(state: BasicState<Int>) {
        when (state) {
            is BasicState.SuccessState<*> -> {
                binding.tvFriendsRequestsNumber.visibility = View.VISIBLE
                val count = (state.data as Int)
                if (count == 0) {
                    binding.tvFriendsRequestsNumber.visibility = View.INVISIBLE
                } else {
                    binding.tvFriendsRequestsNumber.visibility = View.VISIBLE
                    val countString: String = if (count > 9) {
                        getString(R.string.large_number_of_friends_requests)
                    } else {
                        count.toString()
                    }
                    binding.tvFriendsRequestsNumber.text = countString
                }
            }
            is BasicState.ErrorState -> {
                (activity as MainActivity).showSnackBar()
                binding.tvFriendsRequestsNumber.visibility = View.GONE
            }
            else -> {}
        }
    }

    private fun checkStateRemoveFriend(state: BasicState<Unit>) {
        if (state is BasicState.ErrorState) {
            (activity as MainActivity).showSnackBar()
        }
    }


    @SuppressLint("NotifyDataSetChanged")
    private fun removeFriend(friendId: Long, position: Int) {
        viewModel.removeFriend(friendId)
        friendsList.removeAt(position)
        if (friendsList.size == fullFriendsList.size) {
            fullFriendsList.removeAt(position)
        } else {
            for (item in fullFriendsList) {
                if (item.id == friendId) {
                    fullFriendsList.remove(item)
                    break
                }
            }
        }
        binding.rvFriends.adapter?.notifyDataSetChanged()
    }

    private fun setOnClickListenerForTryAgainButton() {
        binding.layoutError.tvUpdate.setOnClickListener {
            viewModel.loadFriends()
        }
    }
}