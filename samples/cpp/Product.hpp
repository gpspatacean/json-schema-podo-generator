#pragma once
#include "ModelBase.hpp"

#include <string>
#include <vector>

#include "Review.hpp"
#include "Dimensions.hpp"

namespace podo_generator { 

class Product : public ModelBase {
public:
    Product() = default;

    /* m_productId accessors */
    int GetProductId() const;
    void SetProductId(int value);

    /* m_productName accessors */
    const std::string& GetProductName() const;
    void SetProductName(const std::string& value);

    /* m_price accessors */
    double GetPrice() const;
    void SetPrice(double value);

    /* m_tags accessors */
    const std::vector<std::string>& GetTags() const;
    void SetTags(const std::vector<std::string>& value);

    /* m_reviews accessors */
    const std::vector<Review>& GetReviews() const;
    void SetReviews(const std::vector<Review>& value);

    /* m_dimensions accessors */
    const Dimensions& GetDimensions() const;
    void SetDimensions(const Dimensions& value);

    bool Serialize(rapidjson::Writer<rapidjson::StringBuffer>& writer) const override;
    bool Deserialize(const rapidjson::Value& obj) override;
private:

    /* The unique identifier for a product */
    int m_productId{};

    /* Name of the product */
    std::string m_productName{};

    /* The price of the product */
    double m_price{};

    /* Tags for the product */
    std::vector<std::string> m_tags{};

    /* Reviews of the product */
    std::vector<Review> m_reviews{};

    /*  */
    Dimensions m_dimensions{};
};

} //namespace podo_generator 
