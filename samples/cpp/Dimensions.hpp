#pragma once
#include "ModelBase.hpp"

#include <string>
#include <vector>

#include "SubDim.hpp"

namespace podo_generator { 

class Dimensions : public ModelBase {
public:
    Dimensions() = default;

    /* m_length accessors */
    double GetLength() const;
    void SetLength(double value);

    /* m_width accessors */
    double GetWidth() const;
    void SetWidth(double value);

    /* m_height accessors */
    double GetHeight() const;
    void SetHeight(double value);

    /* m_subDim accessors */
    const SubDim& GetSubDim() const;
    void SetSubDim(const SubDim& value);

    bool Serialize(rapidjson::Writer<rapidjson::StringBuffer>& writer) const override;
    bool Deserialize(const rapidjson::Value& obj) override;
private:

    /*  */
    double m_length{};

    /*  */
    double m_width{};

    /*  */
    double m_height{};

    /*  */
    SubDim m_subDim{};
};

} //namespace podo_generator 
