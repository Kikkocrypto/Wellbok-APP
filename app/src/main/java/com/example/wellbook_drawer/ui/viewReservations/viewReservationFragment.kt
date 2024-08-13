package com.example.wellbook_drawer.ui.viewReservations

import android.app.AlertDialog
import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.icu.util.Calendar
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.wellbook_drawer.Database.Database_wellApp
import com.example.wellbook_drawer.Database.ReservationDB.Reservation
import com.example.wellbook_drawer.Database.ReservationDB.ReservationDAO
import com.example.wellbook_drawer.Database.UserDB.User
import com.example.wellbook_drawer.R
import com.example.wellbook_drawer.ui.aggiungiPrenotazione.AddReservation_ViewModel
import com.example.wellbook_drawer.ui.aggiungiPrenotazione.ReservationVMFactory
import com.example.wellbook_drawer.ui.viewUsers.ViewUsersFragment.Companion.ARG_COLUMN_COUNT
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Locale

/**
 * A fragment representing a list of Items.
 */
class viewReservationFragment : Fragment() {
    private lateinit var reservationDao: ReservationDAO
    private lateinit var reservationRecyclerViewAdapter: MyItemRecyclerViewAdapter
    private lateinit var viewModel: AddReservation_ViewModel

    private var columnCount = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            columnCount = it.getInt(ARG_COLUMN_COUNT)
        }

        val reservationDatabase = Database_wellApp.getInstance(requireContext())
        reservationDao = reservationDatabase.reservationDAO()
        val factory = ReservationVMFactory(reservationDao)
        viewModel = ViewModelProvider(this, factory).get(AddReservation_ViewModel::class.java)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_reservation_list, container, false)
        val recyclerView: RecyclerView = view.findViewById(R.id.reservation_list)

        reservationRecyclerViewAdapter = MyItemRecyclerViewAdapter(
            clickListener = { reservationWithUser ->
                Toast.makeText(context, "Clicked: ${reservationWithUser.user.name}", Toast.LENGTH_SHORT).show()
            },
            deleteClickListener = { userAndReservation ->
                deleteReservation(userAndReservation.reservation)
            },
            editClickListener = { userAndReservation ->
                showEditDialog(userAndReservation.reservation)
            })

        recyclerView.layoutManager = when {
            columnCount <= 1 -> LinearLayoutManager(context)
            else -> GridLayoutManager(context, columnCount)
        }
        recyclerView.adapter = reservationRecyclerViewAdapter

        loadReservationsFromDB()

        return view
    }



    companion object {

        // TODO: Customize parameter argument names
        const val ARG_COLUMN_COUNT = "column-count"

        // TODO: Customize parameter initialization
        @JvmStatic
        fun newInstance(columnCount: Int) =
            viewReservationFragment().apply {
                arguments = Bundle().apply {
                    putInt(ARG_COLUMN_COUNT, columnCount)
                }
            }
    }


    private fun loadReservationsFromDB() {
        viewModel.getAllReservationsWithUsers().observe(viewLifecycleOwner) { userAndReservations ->
            reservationRecyclerViewAdapter.setList(userAndReservations)
        }
    }

    private fun deleteReservation(reservation: Reservation) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO){
                reservationDao.deleteReservation(reservation)
            }
        }
    }

    private fun updateReservation(reservation: Reservation) {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                reservationDao.updateReservation(reservation)
            }
            loadReservationsFromDB()
        }
    }


    // PS. andrebbe creata una classe e all'interno un companion object con le funzioni !
    private fun showDatePickerDialog(etDate : EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(requireContext(), {
                _, selectedYear, selectedMonth, selectedDay ->
            val selectedDate = Calendar.getInstance().apply {
                set(selectedYear, selectedMonth, selectedDay)
            }
            val dateFormat = SimpleDateFormat("EEEE d MMMM", Locale.getDefault())
            val formattedDate = dateFormat.format(selectedDate.time)
            etDate.setText(formattedDate)
        }, year, month, day)
        datePickerDialog.show()
    }

    private fun showTimePickerDialog(etTime: EditText){
        val calendar = Calendar.getInstance()
        val hour = calendar.get(Calendar.HOUR_OF_DAY)
        val minute = calendar.get(Calendar.MINUTE)

        val timePickerDialog = TimePickerDialog(requireContext(), { _, selectedHour, selectedMinute ->
            val selectedTime = String.format("%02d:%02d", selectedHour, selectedMinute)
            etTime.setText(selectedTime)
        }, hour, minute, true)
        timePickerDialog.show()
    }

    private fun showEditDialog(reservation: Reservation) {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_edit_reservation, null)
        val finishTime = dialogView.findViewById<EditText>(R.id.etFinishTime)
        val startTime = dialogView.findViewById<EditText>(R.id.etStartTime)
        val day = dialogView.findViewById<EditText>(R.id.etDay)
        val btnEdit = dialogView.findViewById<Button>(R.id.btnEditReservation)
        val btnDismiss = dialogView.findViewById<Button>(R.id.btnDismiss)

        day.setText(reservation.day)
        startTime.setText(reservation.startTtime)
        finishTime.setText(reservation.finishTtime)

        day.setOnClickListener {
            showDatePickerDialog(day)
        }
        startTime.setOnClickListener {
            showTimePickerDialog(startTime)
        }
        finishTime.setOnClickListener {
            showTimePickerDialog(finishTime)}


        val dialogBuilder = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setTitle("Modifica Prenotazione")
        val dialog = dialogBuilder.create()
        dialog.show()

        btnEdit.setOnClickListener {
            val updatedReservation = reservation.copy(
                startTtime = startTime.text.toString(),
                finishTtime = finishTime.text.toString(),
                day = day.text.toString()
            )
            updateReservation(updatedReservation)
            dialog.dismiss()
        }

        btnDismiss.setOnClickListener {
            dialog.dismiss()
        }

    }
}