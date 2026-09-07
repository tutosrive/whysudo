/*
 * Copyright (c) 2026 tutosrive
 *
 * Author: tutosrive
 * GitHub: https://github.com/tutosrive
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.srm.whysudo.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.srm.whysudo.R
import com.srm.whysudo.models.CommandModelView
import com.srm.whysudo.utils.ProUtils
import com.srm.whysudo.utils.Utils

class CustomRecyclerAdapter(
    val ctx: Context,
    var commandsList: List<CommandModelView>,
    val favoriteCallback: (command: CommandModelView, iconViewer: ImageView) -> Unit,
    val itemCAllback: (id: Int) -> Unit
) : RecyclerView.Adapter<CustomRecyclerAdapter.Holder>() {

    fun setData(commands: List<CommandModelView>): Unit {
        this.commandsList = commands
        notifyDataSetChanged()
    }

    fun getItemPosition(id: Int): Int {
        val command = this.commandsList.find { it.id == id }!!
        return this.commandsList.indexOf(command)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): Holder {
        val inflater = LayoutInflater.from(parent.context)
        return Holder(
            inflater.inflate(
                R.layout.custom_item_list_commands, parent, false
            ),
            favoriteCallback,
            itemCAllback
        )
    }

    override fun onBindViewHolder(
        holder: Holder,
        position: Int
    ) {
        val item = commandsList[position]
        holder.bind(item)
    }

    override fun getItemCount(): Int {
        return commandsList.size
    }

    class Holder(
        val view: View,
        val callbackFavorite: (command: CommandModelView, iconViewer: ImageView) -> Unit,
        val callbackItem: (id: Int) -> Unit
    ) : RecyclerView.ViewHolder(view) {
        val commandText: TextView = view.findViewById<TextView>(R.id.commandItem)
        val commandBadge: ImageView = view.findViewById<ImageView>(R.id.itemListBadge)
        val favoriteIcon: ImageView = view.findViewById<ImageView>(R.id.btnSetFavorite)

        fun bind(command: CommandModelView): Unit {
            setDataInViews(command)
        }

        private fun setDataInViews(command: CommandModelView): Unit {
            commandText.text = command.filename
            setBadgePro(command)
            setBadgeFavorite(command)
            setListeners(command)
        }

        private fun setListeners(command: CommandModelView): Unit {
            setFavoriteListener(command)
            setItemClickListener(command)
        }

        private fun setItemClickListener(command: CommandModelView): Unit {
            view.setOnClickListener {
                callbackItem.invoke(command.id)
            }
        }

        private fun setFavoriteListener(command: CommandModelView): Unit {
            favoriteIcon.setOnClickListener {
                callbackFavorite(command, it as ImageView)
            }
        }

        private fun setBadgePro(cmd: CommandModelView): Unit {
            val badgeId = Utils.getBadgeProFreeIconId(cmd.typeVersion)
            setDrawable(badgeId, commandBadge)
        }

        private fun setBadgeFavorite(cmd: CommandModelView): Unit {
            ProUtils.toggleFavoriteIcon(favoriteIcon, cmd.isFavorite)
        }

        private fun setDrawable(id: Int, badge: ImageView): Unit {
            badge.setImageResource(id)
        }
    }
}