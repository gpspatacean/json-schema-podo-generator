#pragma once
#include <string>

#include "rapidjson/stringbuffer.h"
#include "rapidjson/writer.h"
#include "rapidjson/document.h"

void Write(rapidjson::Writer<rapidjson::StringBuffer>& writer, int i);
void Write(rapidjson::Writer<rapidjson::StringBuffer>& writer, double d);
void Write(rapidjson::Writer<rapidjson::StringBuffer>& writer, bool b);
void Write(rapidjson::Writer<rapidjson::StringBuffer>& writer, const std::string& s);

void Read(const rapidjson::Value& object, int& i);
void Read(const rapidjson::Value& object, double& d);
void Read(const rapidjson::Value& object, bool& b);
void Read(const rapidjson::Value& object, std::string& s);

class ModelBase {
public:
    ModelBase() = default;
    virtual ~ModelBase() = default;

    std::string ToString() const;
    bool FromString(const std::string& s);
    virtual bool Deserialize(const rapidjson::Value& obj) = 0;
    virtual bool Serialize(rapidjson::Writer<rapidjson::StringBuffer>& writer) const = 0;

protected:
    bool InitDocument(const std::string& s, rapidjson::Document& doc);
};