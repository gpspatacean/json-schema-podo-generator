#include <iostream>
#include "Product.hpp"

int main(int argc, char* argv[]) {
    using namespace podo_generator;
    SubDim subDim{};
    subDim.SetDensity(100.0f);
    subDim.SetWeight(200.0f);

    Dimensions dim{};
    dim.SetHeight(100.0);
    dim.SetLength(200.0);
    dim.SetWidth(150.0);
    dim.SetSubDim(subDim);

    std::vector<std::string> tags{"first tag", "second tag", "third tag"};
    Reviews review1{}, review2{};
    review1.SetUser("firstUser");
    review1.SetRate(10);
    review2.SetUser("secondUser");
    review2.SetRate(8);

    std::vector<Reviews> reviews{ review1, review2};
    Product product{};
    product.SetProductName("prodName");
    product.SetProductId(123);
    product.SetPrice(123.0f);
    product.SetDimensions(dim);
    product.SetTags(tags);
    product.SetReviews(reviews);

    const auto res = product.ToString();
    std::cout<<res<<std::endl;

    Product reversed{};
    reversed.FromString(res);
    std::cout<<reversed.GetDimensions().GetSubDim().GetDensity()<<std::endl;

    std::cout<<product.GetProductName()<<std::endl;
    return 0;
}