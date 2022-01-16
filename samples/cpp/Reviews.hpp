#pragma once
#include "ModelBase.hpp"

#include <string>
#include <vector>


namespace podo_generator { 

class Reviews : public ModelBase {
public:
    Reviews() = default;

    /* m_rate accessors */
    int GetRate() const;
    void SetRate(int value);

    /* m_user accessors */
    const std::string& GetUser() const;
    void SetUser(const std::string& value);

    bool Serialize(rapidjson::Writer<rapidjson::StringBuffer>& writer) const override;
    bool Deserialize(const rapidjson::Value& obj) override;
private:

    /* Rating from 1 to 10 */
    int m_rate{};

    /* User that gave this review */
    std::string m_user{};
};

} //namespace podo_generator 
