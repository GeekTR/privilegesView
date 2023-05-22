package cn.geektang.privilegesview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import cn.geektang.privilegesview.widget.VipPrivilegesItemView

class MainActivity : AppCompatActivity() {
    private lateinit var rvVipPrivileges: RecyclerView
    private val adapter = VipPrivilegesAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        rvVipPrivileges = findViewById(R.id.rvVipPrivileges)
        rvVipPrivileges.adapter = adapter

        val data = mutableListOf<VipPrivilegesItem>()
        data.add(VipPrivilegesItem(text = "1.免费解锁996功能"))
        data.add(
            VipPrivilegesItem(
                text = "2.享受无限制高额大饼，每天都可以看老板画饼哦～",
                action = "立即开通"
            )
        )
        data.add(VipPrivilegesItem(text = "3.可以免费加班到凌晨6点，无需任何加班费"))
        data.add(VipPrivilegesItem(text = "4.每天可以带薪上厕所5分钟"))
        data.add(VipPrivilegesItem(text = "5.更多特权等你解锁,点击了解更多", action = "点我解锁"))
        adapter.setNewData(data)
    }

    class VipPrivilegesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val vipPrivilegesItemView: VipPrivilegesItemView =
            itemView.findViewById(R.id.vipPrivilegesItemView)
        val tvAction: TextView = itemView.findViewById(R.id.tvAction)
    }

    private class VipPrivilegesAdapter : RecyclerView.Adapter<VipPrivilegesViewHolder>() {
        private var data: List<VipPrivilegesItem> = emptyList()

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VipPrivilegesViewHolder {
            val itemView = LayoutInflater.from(parent.context)
                .inflate(R.layout.item_vip_privileges, parent, false)
            return VipPrivilegesViewHolder(itemView)
        }

        override fun getItemCount() = data.size

        override fun onBindViewHolder(holder: VipPrivilegesViewHolder, position: Int) {
            val itemData = data[position]
            holder.vipPrivilegesItemView.text = itemData.text
            holder.tvAction.text = itemData.action
            holder.tvAction.isVisible = !itemData.action.isNullOrEmpty()
        }

        fun setNewData(data: List<VipPrivilegesItem>) {
            this.data = data
            notifyDataSetChanged()
        }
    }

    private class VipPrivilegesItem(
        val text: String,
        val action: String? = null,
    )
}