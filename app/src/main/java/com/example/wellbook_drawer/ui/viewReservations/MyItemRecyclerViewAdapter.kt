package com.example.wellbook_drawer.ui.viewReservations

import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.wellbook_drawer.Database.ReservationDB.Reservation
import com.example.wellbook_drawer.Database.UserAndReservation
import com.example.wellbook_drawer.Database.UserDB.User
import com.example.wellbook_drawer.R

import com.example.wellbook_drawer.ui.viewReservations.placeholder.PlaceholderContent.PlaceholderItem
import com.example.wellbook_drawer.databinding.FragmentReservationBinding
import com.example.wellbook_drawer.databinding.FragmentViewUsersBinding
import com.example.wellbook_drawer.ui.aggiungiUtente.UserViewHolder
import com.google.android.material.floatingactionbutton.FloatingActionButton


class MyItemRecyclerViewAdapter(
    private val clickListener: (UserAndReservation) -> Unit,
    private val deleteClickListener: (UserAndReservation) -> Unit,
    private val editClickListener: (UserAndReservation) -> Unit
) : RecyclerView.Adapter<MyItemRecyclerViewAdapter.ReservationViewHolder>() {

    private val reservationList = ArrayList<UserAndReservation>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReservationViewHolder {
        val binding = FragmentReservationBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReservationViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReservationViewHolder, position: Int) {
        holder.bind(reservationList[position], clickListener, deleteClickListener, editClickListener)
    }

    override fun getItemCount(): Int = reservationList.size

    fun setList(reservations: List<UserAndReservation>) {
        reservationList.clear()
        reservationList.addAll(reservations)
        notifyDataSetChanged()
    }

    inner class ReservationViewHolder(private val binding: FragmentReservationBinding) :
        RecyclerView.ViewHolder(binding.root) {

        private val deleteButton: FloatingActionButton = itemView.findViewById(R.id.fabDeleteReservation)
        private val editButton: FloatingActionButton = itemView.findViewById(R.id.fabEditReservation)

        fun bind(reservationWithUser: UserAndReservation, clickListener: (UserAndReservation) -> Unit,
                 deleteClickListener: (UserAndReservation) -> Unit,
                 editClickListener: (UserAndReservation) -> Unit) {

            binding.apply {
                // Accesso al nome e cognome dell'utente
                tvUser.text = "${reservationWithUser.user.name} ${reservationWithUser.user.lastName}"
                tvStartTime.text = reservationWithUser.reservation.startTtime
                tvFinishTime.text = reservationWithUser.reservation.finishTtime
                tvDay.text = reservationWithUser.reservation.day

                deleteButton.setOnClickListener { deleteClickListener(reservationWithUser) }
                editButton.setOnClickListener { editClickListener(reservationWithUser) }

                root.setOnClickListener {
                    clickListener(reservationWithUser)
                }
            }
        }
    }
}