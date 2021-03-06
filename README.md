

# WheelSelector
An android view for selecting items like a spinning wheel

## How to use

![Demo](https://github.com/Oziomajnr/WheelSelector/blob/master/demo.gif)

Add jitpack in your root build.gradle at the end of repositories:

```css
	allprojects {
		repositories {
			maven { url 'https://jitpack.io'}
		}
	}
```

**Step 2.**  Add the dependency

```css
	dependencies {
	        implementation 'com.github.Oziomajnr:WheelSelector:v1.0.0'
	}
```
 
 Add the view in your layout file 

    <ogbe.ozioma.com.wheelselector.WheelSelectorView  
      android:id="@+id/wheel_selector_view"  
      android:layout_width="match_parent"  
      android:layout_height="wrap_content"  
      app:itemBarColor="#B81D1D"  
      app:itemBarHeight="100dp"  
      app:itemBarThickness="2dp"  
      app:layout_constraintBottom_toBottomOf="parent"  
      app:layout_constraintLeft_toLeftOf="parent"  
      app:layout_constraintRight_toRightOf="parent"  
      app:layout_constraintTop_toTopOf="parent"  
      app:selectedMarkerDrawable="@drawable/ic_chevron_down"  
      app:selectedMarkerUnit="x" />

you can customize the look by setting the following attributes

    app:itemBarColor="#B81D1D"  
    app:itemBarHeight="100dp"  
    app:itemBarThickness="2dp"  
    app:selectedMarkerDrawable="@drawable/ic_chevron_down"  
    app:selectedMarkerUnit="V"

Each wheel item is modelled with the following parameters

       data class WheelSelectorItem(val value: Float,val highlight: Boolean = false, var selected: Boolean = false)

From your activity or fragment, you can set a list of items to be displayed in the wheel :

    private val items: List<WheelSelectorItem> = listOf(  
    WheelSelectorItem(0.5f, true),  
      WheelSelectorItem(0.6f),  
      WheelSelectorItem(0.7f),  
      WheelSelectorItem(0.8f),  
      WheelSelectorItem(0.9f),  
      WheelSelectorItem(1.0f, true),  
      WheelSelectorItem(1.1f),  
      WheelSelectorItem(1.2f),  
      WheelSelectorItem(1.3f),  
      WheelSelectorItem(1.4f),  
      WheelSelectorItem(1.5f, true),  
      WheelSelectorItem(1.6f),  
      WheelSelectorItem(1.7f),  
      WheelSelectorItem(1.8f),  
      WheelSelectorItem(1.9f),  
      WheelSelectorItem(2.0f, true)  
    )  
      
    override fun onCreate(savedInstanceState: Bundle?) {  
        super.onCreate(savedInstanceState)  
        setContentView(R.layout.activity_main)  
        wheelSelectorView.setItems(items) 
    }

you can manually select an item using the method 

     wheelSelectorView.setSelectedItem(WheelSelectorItem(1.4f)) 
To get notified when an item is selected, register a SpeedSelectorEvent listener e.g

    wheelSelectorView.itemSelectedEvent = object : SpeedSelectorEvent {  
        override fun onItemSelected(wheelSelectorItem: WheelSelectorItem) {  
            Toast.makeText(  
                this@MainActivity,  
      wheelSelectorItem.value.toString(),  
      Toast.LENGTH_SHORT  
      ).show()  
        }  
    }

Check out the [sample project](https://github.com/Oziomajnr/WheelSelector/tree/master/app) to see it in action

Credits: 
[Center Decorator](https://stackoverflow.com/a/53510142/3937903)


