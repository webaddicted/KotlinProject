package com.webaddicted.kotlinproject.view.ecommerce

import android.os.Bundle
import android.view.View
import android.view.animation.AnimationUtils
import androidx.databinding.ViewDataBinding
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.webaddicted.kotlinproject.R
import com.webaddicted.kotlinproject.databinding.FrmEcomHomeBinding
import com.webaddicted.kotlinproject.global.common.Lg
import com.webaddicted.kotlinproject.model.bean.ecommerce.EcommCateBean
import com.webaddicted.kotlinproject.view.adapter.EcomFashionAdapter
import com.webaddicted.kotlinproject.view.adapter.EcomHomeDecoAdapter
import com.webaddicted.kotlinproject.view.adapter.EcomMobileAdapter
import com.webaddicted.kotlinproject.view.base.BaseAdapter
import com.webaddicted.kotlinproject.view.base.BaseFragment
import ss.com.bannerslider.banners.Banner
import ss.com.bannerslider.banners.RemoteBanner
import java.util.*

class EcommHomeFrm : BaseFragment(R.layout.frm_ecom_home) {
    private var ecommHomeDeco: ArrayList<EcommCateBean>? = null
    private var ecommMobile: ArrayList<EcommCateBean>? = null
    private var ecommFashion: ArrayList<EcommCateBean>? = null
    private lateinit var ecomMobileAdapter: EcomMobileAdapter
    private lateinit var ecomHomeDecAdapter: EcomHomeDecoAdapter
    private lateinit var ecomFashionAdapter: EcomFashionAdapter
    private lateinit var mBinding: FrmEcomHomeBinding

    companion object {
        val TAG = EcommHomeFrm::class.qualifiedName
        fun getInstance(bundle: Bundle): EcommHomeFrm {
            val fragment = EcommHomeFrm()
            fragment.arguments = bundle
            return fragment
        }
    }
    override fun onBindTo(binding: ViewDataBinding?) {
        mBinding = binding as FrmEcomHomeBinding
        init()
        clickListener()
    }

    private fun init() {
        addDataBean()
        ecomFashionAdapter = EcomFashionAdapter(this, ecommFashion)
        ecomMobileAdapter = EcomMobileAdapter(this, ecommMobile)
        ecomHomeDecAdapter = EcomHomeDecoAdapter(this, ecommHomeDeco)
        (activity as EcommHomeActivity).setNavi(mBinding.imgNavi)
        setAdapter(mBinding.rvFashion, ecomFashionAdapter)
        setAdapter(mBinding.rvMobileElec, ecomMobileAdapter)
        setAdapter(mBinding.rvHomeDesc, ecomHomeDecAdapter)
        addBanners()
    }

    private fun clickListener() {
        mBinding.imgNavi.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        super.onClick(v)
        when (v.id) {
            R.id.img_navi -> (activity as EcommHomeActivity).openCloseDrawer(true)
        }
    }


    private fun addDataBean() {
        ecommFashion = ArrayList<EcommCateBean>()
        ecommMobile = ArrayList<EcommCateBean>()
        ecommHomeDeco = ArrayList<EcommCateBean>()

        ecommFashion?.add(EcommCateBean().apply {
            catName = "Mens Wear"
            catImg = "https://images-na.ssl-images-amazon.com/images/I/415cjMAjKbL.jpg"
        })
        ecommFashion?.add(EcommCateBean().apply {
            catName = "Kids Wear"
            catImg =
                "https://www.dhresource.com/600x600/f2/albu/g9/M01/E3/A1/rBVaWFy_8LOAFZQ5AAHPt71dTi0446.jpg"
        })
        ecommFashion?.add(EcommCateBean().apply {
            catName = "Womens Wear"
            catImg = "https://i.pinimg.com/474x/83/6e/81/836e8193eb889280ea5c8b93eec313a0.jpg"
        })
        ecommFashion?.add(EcommCateBean().apply {
            catName = "Girl Wear Dress"
            catImg =
                "https://dynamic.zacdn.com/FE9vFhq-I-uGtdk0E1O7vHe3uP0=/fit-in/346x500/filters:quality(95):fill(ffffff)/http://static.hk.zalora.net/p/crystal-korea-fashion-5582-0726384-1.jpg"
        })
        ecommFashion?.add(EcommCateBean().apply {
            catName = "Korean Wear"
            catImg =
                "https://cdn.shopify.com/s/files/1/0974/6976/products/4571857896_938251155_grande.jpg"
        })
        ecommFashion?.add(EcommCateBean().apply {
            catName = "Boys Wear"
            catImg = "https://img1.cfcdn.club/e9/58/e95cbf0b430cb855fb5e33528f308458_350x350.jpg"
        })

        ecommMobile?.add(EcommCateBean().apply {
            catName = "Smart Phones"
            catImg =
                "https://images.naptol.com/usr/local/csp/staticContent/product_images/horizontal/750x750/LifePlus-Big-Screen-4G-Calling-Tablet-with-Keyboard-2.jpg"
        })
        ecommMobile?.add(EcommCateBean().apply {
            catName = "Computer"
            catImg =
                "https://www.energy.gov/sites/prod/files/styles/borealis_photo_gallery_large_respondmedium/public/computer%20tablet%20phone.jpg?itok=QXuExAH7"
        })
        ecommMobile?.add(EcommCateBean().apply {
            catName = "Camera"
            catImg =
                "https://www.ft.com/__origami/service/image/v2/images/raw/https%3A%2F%2Fs3-ap-northeast-1.amazonaws.com%2Fpsh-ex-ftnikkei-3937bb4%2Fimages%2F5%2F1%2F8%2F0%2F20370815-2-eng-GB%2FCropped-1555509431RTS22VSA.jpg"
        })
        ecommMobile?.add(EcommCateBean().apply {
            catName = "Smart Watch"
            catImg =
                "https://rukminim1.flixcart.com/image/832/832/jd1z9u80/smartwatch/9/9/q/button-position-in-the-watch-may-vary-slightly-gt08-watch-original-imaffxefgcyss9qj.jpeg?q=70"
        })
        ecommMobile?.add(EcommCateBean().apply {
            catName = "TV"
            catImg =
                "https://azcd.harveynorman.com.au/media/catalog/product/5/5/55inch-akai-image_1.jpg"
        })
        ecommMobile?.add(EcommCateBean().apply {
            catName = "Speaker"
            catImg =
                "https://www.voylla.com/dare-blog//wp-content/uploads/2017/03/Electronics-Gadget-blog-02.jpg"
        })


        ecommHomeDeco?.add(EcommCateBean().apply {
            catName = "Home Appliances"
            catImg =
                "https://rukminim1.flixcart.com/image/612/612/jmi22kw0/mixer-grinder-juicer/n/z/a/butterfly-rapid-4-jar-750-watts-original-imaf5dvgugcqyug3.jpeg?q=70"
        })
        ecommHomeDeco?.add(EcommCateBean().apply {
            catName = "Cookware"
            catImg = "https://www.stovekraft.com/upload/pigeon/slider/1455279421959.png"
        })
        ecommHomeDeco?.add(EcommCateBean().apply {
            catName = "Kitchen Appliances"
            catImg = "https://www.stovekraft.com/upload/pigeon/slider/1463050089114.png"
        })
        ecommHomeDeco?.add(EcommCateBean().apply {
            catName = "HomeAppliances"
            catImg =
                "https://rukminim1.flixcart.com/image/612/612/jmi22kw0/mixer-grinder-juicer/n/z/a/butterfly-rapid-4-jar-750-watts-original-imaf5dvgugcqyug3.jpeg?q=70"
        })
        ecommHomeDeco?.add(EcommCateBean().apply {
            catName = "Furniture"
            catImg =
                "https://www.thenewsminute.com/sites/default/files/styles/slideshow_image_size/public/Rentickl1.jpg?itok=BNHrj695"
        })
        ecommHomeDeco?.add(EcommCateBean().apply {
            catName = "Electronic"
            catImg = "https://i.pinimg.com/originals/87/9e/d3/879ed3b30ae4c9e1161ea37ff6255af4.jpg"
        })
        ecommHomeDeco?.add(EcommCateBean().apply {
            catName = "Plastic"
            catImg =
                "http://topazgroup.net/wp-content/uploads/2017/06/plastic-cool-water-jug1-300x300.jpg"
        })
        ecommHomeDeco?.add(EcommCateBean().apply {
            catName = "Washing Machine"
            catImg =
                "https://cdnprod.mafretailproxy.com/cdn-cgi/image/format=auto,onerror=redirect/sys-master-prod/hdb/hf8/9018170834974/1198889_main.jpg_480Wx480H"
        })
        ecommHomeDeco?.add(EcommCateBean().apply {
            catName = "Freeze"
            catImg =
                "https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcQfhQ6DleoXydKv2YHiih4EMpMtHT4_LA-Kpw7brO5IVuCukRVqXg&s"
        })


    }

    private fun setAdapter(rvFashion: RecyclerView?, ecomFashionAdapter: BaseAdapter) {
        rvFashion?.layoutManager = GridLayoutManager(activity, 4)
        changeAnimation(rvFashion)
        rvFashion?.adapter = ecomFashionAdapter
    }

    private fun changeAnimation(rvFashion: RecyclerView?) {
        val animation = AnimationUtils.loadLayoutAnimation(context, R.anim.rv_anim_left_to_right)
        rvFashion?.layoutAnimation = animation
    }

    private fun addBanners() {
        val remoteBanners = ArrayList<Banner>()
        //Add banners using image urls
        remoteBanners.add(
            RemoteBanner(
                "https://ace.electronicsforu.com/wp-content/uploads/2019/09/Samsung-Festive-Offers-Diwali-India.jpg"
            )
        )
        remoteBanners.add(
            RemoteBanner(
                "https://i.pinimg.com/600x315/0d/4d/ac/0d4dac5af8191ff4843ac3fb411debdf.jpg"
            )
        )
        remoteBanners.add(
            RemoteBanner(
                "https://shoppingwithlowprice.files.wordpress.com/2014/10/banner_multibuy2.jpg"
            )
        )
        remoteBanners.add(
            RemoteBanner(
                "https://i.ytimg.com/vi/OpQRsFVseMU/maxresdefault.jpg"
            )
        )
        remoteBanners.add(
            RemoteBanner(
                "http://standardmiraclellc.com/wp-content/uploads/2018/08/banner2-min-1030x579.png"
            )
        )
        remoteBanners.add(
            RemoteBanner(
                "http://cdn.shopclues.com/images/mailer/kitchen-banner.jpg"
            )
        )
        remoteBanners.add(
            RemoteBanner(
                "https://www.shoppirate.in/blog/wp-content/uploads/2015/10/Amazon-diwali-sale.jpg"
            )
        )
        //        remoteBanners.add(new DrawableBanner(R.drawable.btn));
        mBinding.bannerSlider.setBanners(remoteBanners)
        TAG?.let { Lg.d(it, "addBanners: " + remoteBanners.size) }
        //        mBinding.bannerSlider.setOnBannerClickListener(new OnBannerClickListener() {
        //            @Override
        //            public void onClick(int position) {
        //                Toast.makeText(getContext(), "Banner with position " + String.valueOf(position) + " clicked!", Toast.LENGTH_SHORT).show();
        //            }
        //        });
    }


    /**
     * navigate on fragment
     *
     * @param tag represent navigation activity
     */
    fun navigateScreen(tag: String?) {
        var frm: Fragment? = null
        when (tag) {
            EcommProductListFrm.TAG -> frm = EcommProductListFrm.getInstance(Bundle())
        }
        if (frm != null) navigateAddFragment(R.id.container, frm, true)
    }

}

