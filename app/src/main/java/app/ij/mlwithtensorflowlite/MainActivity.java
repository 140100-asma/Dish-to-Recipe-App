/*
 * Created by ishaanjav
 * github.com/ishaanjav
*/

package app.ij.mlwithtensorflowlite;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.ThumbnailUtils;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.tensorflow.lite.DataType;
import org.tensorflow.lite.support.tensorbuffer.TensorBuffer;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import app.ij.mlwithtensorflowlite.ml.Dish;

public class MainActivity extends AppCompatActivity {

    TextView result, confidence;
    ImageView imageView;
    Button picture;
    int imageSize = 224;
    String value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        result = findViewById(R.id.result);
        confidence = findViewById(R.id.confidence);
        imageView = findViewById(R.id.image_id);
        picture = findViewById(R.id.button);

        picture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Launch camera if we have permission
                if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                    Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, 1);
                } else {
                    //Request camera permission if we don't have it.
                    requestPermissions(new String[]{Manifest.permission.CAMERA}, 100);
                }
            }

        });

        Button gallery = findViewById(R.id.button2);
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
                intent.setType("image/*");
                startActivityForResult(intent, 3);

            }
        });


    }

    public void classifyImage(Bitmap image) {
        try {
            Dish model = Dish.newInstance(getApplicationContext());

            // Creates inputs for reference.
            TensorBuffer inputFeature0 = TensorBuffer.createFixedSize(new int[]{1, 224, 224, 3}, DataType.FLOAT32);
            ByteBuffer byteBuffer = ByteBuffer.allocateDirect(4 * imageSize * imageSize * 3);
            byteBuffer.order(ByteOrder.nativeOrder());

            // get 1D array of 224 * 224 pixels in image
            int[] intValues = new int[imageSize * imageSize];
            image.getPixels(intValues, 0, image.getWidth(), 0, 0, image.getWidth(), image.getHeight());

            // iterate over pixels and extract R, G, and B values. Add to bytebuffer.
            int pixel = 0;
            for (int i = 0; i < imageSize; i++) {
                for (int j = 0; j < imageSize; j++) {
                    int val = intValues[pixel++]; // RGB
                    byteBuffer.putFloat(((val >> 16) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat(((val >> 8) & 0xFF) * (1.f / 255.f));
                    byteBuffer.putFloat((val & 0xFF) * (1.f / 255.f));
                }
            }
            
            inputFeature0.loadBuffer(byteBuffer);

            // Runs model inference and gets result.
            Dish.Outputs outputs = model.process(inputFeature0);
            TensorBuffer outputFeature0 = outputs.getOutputFeature0AsTensorBuffer();

            float[] confidences = outputFeature0.getFloatArray();
            // find the index of the class with the biggest confidence.
            int maxPos = 0;
            float maxConfidence = 0;
            for (int i = 0; i < confidences.length; i++) {
                if (confidences[i] > maxConfidence) {
                    maxConfidence = confidences[i];
                    maxPos = i;
                }
            }

            String[] classes = {"Riz aux lentilles", "Du riz bouillon brede", "Bol renverse", "Chicken kalia", "Khuri Kitchri", "La daube", "Halim", "Pizza",
                    "Biryani",
                    "Gateaux piment(chilli poppers)",
                    "Dhal puri",
                    "Fried rice",
                    "Chicken stew",
                    "Dhal pita",
                    "Spaghetti bolognese",
                    "Tandoori chicken masala",
                    "Soupe mais",
                    "Vindaye",
                    "Lasagna",
                    "Quiche",
                    "Hummus",
                    "Sushi",
                    "Chilli con carne",
                    "Fried noodles",
                    "Butter chicken",
                    "Mac and cheese",
                    "Couscous",
                    "Shish kebab",
                    "Burger with chips",
                    "Du riz dhal",
                    "Garlic naan",
                    "Sept cari",
                    "Mine bouille",
                    "Rougaille",
                    "Dosa",
                    "Chinese Dim Sum",
                    "Boulettes"};
            result.setText(classes[maxPos]);

            String s = "";
            for (int i = 0; i < classes.length; i++) {
                s += String.format("%s: %.1f%%\n", classes[i], confidences[i] * 100);
            }
            confidence.setText(s);


            // Releases model resources if no longer used.
            model.close();
        } catch (IOException e) {
            // TODO Handle the exception

        }
    }
            @Override
            public void onActivityResult ( int requestCode, int resultCode, @Nullable Intent data){
                super.onActivityResult(requestCode, resultCode, data);
                if (requestCode == 1 && resultCode == RESULT_OK) {
                    Bitmap image = (Bitmap) data.getExtras().get("data");
                    int dimension = Math.min(image.getWidth(), image.getHeight());
                    image = ThumbnailUtils.extractThumbnail(image, dimension, dimension);
                    imageView.setImageBitmap(image);
                    image = Bitmap.createScaledBitmap(image, imageSize, imageSize, false);
                    classifyImage(image);
                }else if (requestCode == 3 && resultCode == RESULT_OK && data != null){
                    try {
                        InputStream inputStream = getContentResolver().openInputStream(data.getData());
                        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                        int dimension = Math.min(bitmap.getWidth(), bitmap.getHeight());
                        bitmap = ThumbnailUtils.extractThumbnail(bitmap, dimension, dimension);
                        imageView.setImageBitmap(bitmap);
                        bitmap = Bitmap.createScaledBitmap(bitmap, imageSize, imageSize, false);
                        classifyImage(bitmap);

                        //to show button here:
                        Button showRecipe=(Button) findViewById(R.id.showRecipe2);

                        showRecipe.setOnClickListener(new View.OnClickListener(){

                            public void onClick(View v){
                                value=result.getText().toString ();
                                Intent intent = new Intent(MainActivity.this, ShowRecipe.class);
                                intent.putExtra ("key", value);
                                startActivity(intent);
//                                Toast.makeText(MainActivity.this, value, Toast.LENGTH_SHORT).show();
                            }
                        });



                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }

            }



        }
