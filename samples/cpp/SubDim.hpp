#pragma once
#include "ModelBase.hpp"

#include <string>
#include <vector>


namespace podo_generator { 

class SubDim : public ModelBase {
public:
    SubDim() = default;

    /* m_weight accessors */
    double GetWeight() const;
    void SetWeight(double value);

    /* m_density accessors */
    double GetDensity() const;
    void SetDensity(double value);

    bool Serialize(rapidjson::Writer<rapidjson::StringBuffer>& writer) const override;
    bool Deserialize(const rapidjson::Value& obj) override;
private:

    /* The weight of the product */
    double m_weight{};

    /* The density of the product */
    double m_density{};
};

} //namespace podo_generator 
