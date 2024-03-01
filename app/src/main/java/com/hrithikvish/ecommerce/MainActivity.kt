package com.hrithikvish.ecommerce

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Html
import android.view.View
import android.view.ViewTreeObserver
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.hrithikvish.ecommerce.adapter.ColorsListRVAdapter
import com.hrithikvish.ecommerce.adapter.ViewPagerAdapter
import com.hrithikvish.ecommerce.databinding.ActivityMainBinding
import com.hrithikvish.ecommerce.models.Attributes
import com.hrithikvish.ecommerce.models.Product
import com.hrithikvish.ecommerce.viewmodel.ProductViewModel
import com.hrithikvish.ecommerce.viewmodel.ProductViewModelFactory

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding

    private lateinit var colorsRVAdapter : ColorsListRVAdapter
    private lateinit var productViewModel : ProductViewModel

    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewpager.viewTreeObserver.addOnGlobalLayoutListener(object : ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                val width = binding.viewpager.width
                val layoutParams = binding.viewpager.layoutParams
                layoutParams.height = width
                binding.viewpager.layoutParams = layoutParams
                binding.viewpager.viewTreeObserver.removeOnGlobalLayoutListener(this)
            }
        })

        val repository = (application as ECommerceApplication).productRepo
        productViewModel = ViewModelProvider(this, ProductViewModelFactory(repository)).get(ProductViewModel::class.java)

        productViewModel.products.observe(this, Observer {
            setFirstProductInViewPager(it)
            setTextViews(it)
            initColorsRVAdapter(it)
            setClickListeners(it)
        })

        productViewModel.progBarVisibility.observe(this, Observer {isVisible ->
            if(isVisible) {
                binding.colorsRvProgressBar.visibility = View.VISIBLE
                binding.vpProgressBar.visibility = View.VISIBLE
            } else {
                binding.colorsRvProgressBar.visibility = View.GONE
                binding.vpProgressBar.visibility = View.GONE
            }
        })

    }

    private fun setFirstProductInViewPager(product: Product) {
        val firstProduct: Attributes = product.data.configurable_option.get(0).attributes.get(0)

        lateinit var viewPagerAdapter: ViewPagerAdapter
        val imagesList: ArrayList<String> = ArrayList()
        imagesList.add(firstProduct.images.get(0))
        imagesList.add(firstProduct.images.get(1))
        viewPagerAdapter = ViewPagerAdapter(this, imagesList)
        binding.viewpager.adapter = viewPagerAdapter
        binding.dotsIndicator.attachTo(binding.viewpager)
        binding.vpProgressBar.visibility = View.GONE

    }

    private fun setTextViews(it: Product) {
        binding.brandName.text = it.data.brand_name
        binding.name.text = it.data.name
        binding.sku.text = it.data.sku
        binding.price.text = String.format("%.2f", it.data.price) + " KWD"
        binding.prodInfoData.text = Html.fromHtml(it.data.description)
    }

    private fun initColorsRVAdapter(it: Product) {
        colorsRVAdapter = ColorsListRVAdapter(this, binding)
        colorsRVAdapter.setColorsList(it.data.configurable_option.get(0).attributes)
        colorsRVAdapter.notifyDataSetChanged()
        binding.colorsRv.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        binding.colorsRv.adapter = colorsRVAdapter
    }

    @SuppressLint("SetTextI18n")
    private fun setClickListeners(product: Product) {

        binding.backArrow.setOnClickListener {
            finish()
        }

        binding.minusItemBtn.setOnClickListener {
            if (Integer.parseInt(binding.itemCountEt.text.toString()) > 1) {
                binding.itemCountEt.setText((Integer.parseInt(binding.itemCountEt.text.toString()) - 1).toString())
            }
        }
        binding.addItemBtn.setOnClickListener {
            binding.itemCountEt.setText((Integer.parseInt(binding.itemCountEt.text.toString()) + 1).toString())
        }

        binding.showInfoBtn.setOnClickListener {
            binding.prodInfoData.maxLines = Int.MAX_VALUE
            binding.showInfoBtn.visibility = View.INVISIBLE
            binding.hideInfoBtn.visibility = View.VISIBLE
        }

        binding.hideInfoBtn.setOnClickListener {
            binding.prodInfoData.maxLines = 4
            binding.hideInfoBtn.visibility = View.INVISIBLE
            binding.showInfoBtn.visibility = View.VISIBLE
        }

        binding.learnMoreTxt.setOnClickListener { view ->
            startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(product.data.web_url)))
        }

        binding.shareBtn.setOnClickListener {
            val sendIntent: Intent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, product.data.web_url)
                type = "text/plain"
            }
            val shareIntent = Intent.createChooser(sendIntent, "Share " + product.data.brand_name + " " + product.data.name)
            startActivity(shareIntent)
        }

    }

}